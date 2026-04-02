package rosh.myrosh.pop

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import rosh.myrosh.R
import java.io.File
import java.io.FileOutputStream

class DataLogin(private val kelas: AppCompatActivity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager: CredentialManager = CredentialManager.create(kelas)

    fun Tampil() {
        val item = LayoutInflater.from(kelas).inflate(R.layout.data_login, null)
        val builder = AlertDialog.Builder(kelas).setView(item).create()
        val foto = item.findViewById<ImageView>(R.id.foto)
        val nama = item.findViewById<TextView>(R.id.nama)
        val email = item.findViewById<TextView>(R.id.email)
        val no = item.findViewById<TextView>(R.id.nomor)
        val batal = item.findViewById<TextView>(R.id.batal)
        val mendaftar = item.findViewById<TextView>(R.id.mendaftar)

        fun muatData() {
            val data = AmbilDataAkun()
            mendaftar.visibility = if (data.google) View.GONE else View.VISIBLE
            nama.text = "Nama : ${data.nama}"
            email.text = "Email : ${data.email}"
            no.text = "No : ${FormatNomor(data.nomor)}"
            MuatFoto(foto, data)
        }

        batal.setOnClickListener {
            builder.dismiss()
        }

        mendaftar.setOnClickListener {
            kelas.lifecycleScope.launch {
                val berhasil = MasukGoogle()
                if (berhasil) {
                    muatData()
                    Toast.makeText(kelas, "Masuk dengan Google", Toast.LENGTH_SHORT).show()
                }
            }
        }

        muatData()
        builder.show()
    }

    fun Ambil(): UserLogin? {
        val data = AmbilDataAkun()
        if (!data.google && data.nama == "Belum masuk") {
            return null
        }

        val gambar = data.fotoPath
            ?.takeIf { File(it).exists() }
            ?.let { BitmapFactory.decodeFile(it) }

        return UserLogin(
            nama = data.nama.ifBlank { "Tamu" },
            pp = gambar
        )
    }

    private fun AmbilDataAkun(): DataAkun {
        val user = auth.currentUser
        if (user != null) {
            val data = DataAkun(
                nama = user.displayName ?: "Google",
                email = user.email ?: "-",
                nomor = user.phoneNumber,
                google = true,
                fotoUrl = user.photoUrl?.toString(),
                fotoPath = File(FolderLogin(), "profil.jpg").absolutePath
            )
            kelas.lifecycleScope.launch {
                SimpanDataLokal(data)
            }
            return data
        }

        val simpan = kelas.getSharedPreferences("data_login", Context.MODE_PRIVATE)
        val google = simpan.getBoolean("google_login", false)

        return DataAkun(
            nama = simpan.getString("nama", "Belum masuk").orEmpty(),
            email = simpan.getString("email", "-").orEmpty(),
            nomor = simpan.getString("nomor", null),
            google = google,
            fotoUrl = null,
            fotoPath = simpan.getString("foto_path", null)
        )
    }

    private suspend fun MasukGoogle(): Boolean {
        return try {
            val credential = AmbilKredensialGoogle(filterAuthorized = true)
                ?: AmbilKredensialGoogle(filterAuthorized = false)
                ?: return false

            if (credential !is CustomCredential ||
                credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                Toast.makeText(kelas, "Kredensial Google tidak cocok", Toast.LENGTH_SHORT).show()
                return false
            }

            val googleCredential = try {
                GoogleIdTokenCredential.createFrom(credential.data)
            } catch (_: GoogleIdTokenParsingException) {
                Toast.makeText(kelas, "Token Google tidak valid", Toast.LENGTH_SHORT).show()
                return false
            }

            val firebaseCredential = GoogleAuthProvider.getCredential(googleCredential.idToken, null)
            auth.signInWithCredential(firebaseCredential).await()
            auth.currentUser?.let { SimpanDariFirebase(it) }
            true
        } catch (_: GetCredentialException) {
            Toast.makeText(kelas, "Pemilihan akun Google dibatalkan", Toast.LENGTH_SHORT).show()
            false
        } catch (_: Exception) {
            Toast.makeText(kelas, "Gagal masuk dengan Google", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private suspend fun AmbilKredensialGoogle(filterAuthorized: Boolean): androidx.credentials.Credential? {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(kelas.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(filterAuthorized)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            credentialManager.getCredential(kelas, request).credential
        } catch (_: GetCredentialException) {
            null
        }
    }

    private suspend fun SimpanDariFirebase(user: FirebaseUser) {
        val data = DataAkun(
            nama = user.displayName ?: "Google",
            email = user.email ?: "-",
            nomor = user.phoneNumber,
            google = true,
            fotoUrl = user.photoUrl?.toString(),
            fotoPath = File(FolderLogin(), "profil.jpg").absolutePath
        )
        SimpanDataLokal(data)
    }

    private suspend fun SimpanDataLokal(data: DataAkun) {
        val folder = FolderLogin()
        val fileNama = File(folder, "nama.txt")
        val fileFoto = File(folder, "profil.jpg")

        withContext(Dispatchers.IO) {
            folder.mkdirs()
            fileNama.writeText(data.nama)

            if (!data.fotoUrl.isNullOrBlank()) {
                try {
                    val bitmap = Glide.with(kelas)
                        .asBitmap()
                        .load(data.fotoUrl)
                        .submit()
                        .get()
                    SimpanBitmap(fileFoto, bitmap)
                } catch (_: Exception) {
                }
            }
        }

        kelas.getSharedPreferences("data_login", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("google_login", true)
            .putString("nama", data.nama)
            .putString("email", data.email)
            .putString("nomor", data.nomor)
            .putString("foto_path", fileFoto.absolutePath)
            .apply()
    }

    private fun FormatNomor(nomor: String?): String {
        if (nomor.isNullOrBlank()) {
            return "123-8"
        }

        val digitTerakhir = nomor.filter { it.isDigit() }.lastOrNull() ?: nomor.last()
        return "**$digitTerakhir"
    }

    private fun MuatFoto(foto: ImageView, data: DataAkun) {
        val path = data.fotoPath
        if (!path.isNullOrBlank() && File(path).exists()) {
            Glide.with(kelas)
                .load(File(path))
                .placeholder(R.drawable.ikon_pp)
                .error(R.drawable.ikon_pp)
                .circleCrop()
                .into(foto)
            return
        }

        if (!data.fotoUrl.isNullOrBlank()) {
            Glide.with(kelas)
                .load(Uri.parse(data.fotoUrl))
                .placeholder(R.drawable.ikon_pp)
                .error(R.drawable.ikon_pp)
                .circleCrop()
                .into(foto)
            return
        }

        Glide.with(kelas)
            .load(R.drawable.ikon_pp)
            .circleCrop()
            .into(foto)
    }

    private fun FolderLogin(): File {
        val dasar = kelas.getExternalFilesDir(null) ?: kelas.filesDir
        return File(dasar, "login")
    }

    private fun SimpanBitmap(target: File, bitmap: Bitmap) {
        FileOutputStream(target).use { keluar ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, keluar)
            keluar.flush()
        }
    }

    data class UserLogin(
        val nama: String,
        val pp: Bitmap?
    )

    private data class DataAkun(
        val nama: String,
        val email: String,
        val nomor: String?,
        val google: Boolean,
        val fotoUrl: String?,
        val fotoPath: String?
    )
}
