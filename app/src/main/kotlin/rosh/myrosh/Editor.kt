package rosh.myrosh

import android.os.*
import androidx.appcompat.app.*
import android.widget.*
import androidx.core.view.*
import android.view.*
import androidx.drawerlayout.widget.*
import rosh.myrosh.pop.*
import android.net.Uri
import android.content.Intent
import androidx.documentfile.provider.DocumentFile
import io.github.rosemoe.sora.widget.*
import io.github.rosemoe.sora.widget.schemes.*
import android.graphics.Typeface
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Editor : AppCompatActivity() {

    private lateinit var pusat: DrawerLayout
    private var folder: Uri? = null
    private lateinit var tulis: CodeEditor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editor)

        PasangId()
        Tombol()
        AturEditor()
        val terima = intent.getStringExtra("file")
        if (terima != null) { BukaFile(Uri.parse(terima)) }
    }

    private fun PasangId() {
        pusat = findViewById(R.id.pusat)
        tulis = findViewById(R.id.tulis)
        Terpilih(null)
    }

    private fun Keluar() {
        Peringatan(this).Tampil("Kembali ke beranda", "Yakin ?")
    }

    override fun onBackPressed() {
        if (pusat.isDrawerOpen(GravityCompat.START)) {
            pusat.closeDrawer(GravityCompat.START)
        } else {
            Keluar()
        }
    }

    private fun Tombol() {
        findViewById<TextView>(R.id.keluar).setOnClickListener {
            Keluar()
        }

        findViewById<ImageView>(R.id.nav).setOnClickListener {
            pusat.openDrawer(GravityCompat.START)
        }

        findViewById<ImageView>(R.id.file).setOnClickListener {
            Terpilih(null)
        }
    }

    private fun Terpilih(terima: Uri?) {
        val tempat = findViewById<LinearLayout>(R.id.tempat)
        tempat.removeAllViews()

        if (terima == null) {
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_text, tempat, false)

            val itemText = itemView.findViewById<TextView>(R.id.huruf)
            itemText.text = "Klik untuk memilih folder"
            itemText.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                startActivityForResult(intent, 1)
            }
            tempat.addView(itemView)
        } else {
            val docFile = DocumentFile.fromTreeUri(this, terima)
            if (docFile == null || !docFile.exists()) {
                Toast.makeText(this, "Folder tidak valid", Toast.LENGTH_SHORT).show()
                Terpilih(null)
                return
            }

            TampilFolder(terima)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val selectedUri = data?.data ?: return
            folder = selectedUri

            try {
                contentResolver.takePersistableUriPermission(
                    selectedUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                Toast.makeText(this, "Gagal mengambil izin permanen", Toast.LENGTH_SHORT).show()
                return
            }

            Terpilih(selectedUri)
        }
    }

    private fun AturEditor() {
        tulis.colorScheme = EditorColorScheme()
        tulis.setTextSize(14f)
        tulis.typefaceText = Typeface.MONOSPACE
    }

    private fun BukaFile(uri: Uri) {
        val loadingToast = Toast.makeText(this, "Membuka file...", Toast.LENGTH_SHORT)
        loadingToast.show()

        lifecycleScope.launch {
            try {
                val text = withContext(Dispatchers.IO) {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        inputStream.bufferedReader().readText()
                    } ?: throw Exception("Gagal membuka stream")
                }

                withContext(Dispatchers.Main) {
                    tulis.setText(text)
                    findViewById<TextView>(R.id.nama).text =
                        DocumentFile.fromSingleUri(this@Editor, uri)?.name ?: "Unknown"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Editor, "Gagal buka file: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    loadingToast.cancel()
                }
            }
        }
    }

    private fun TampilFolder(uri: Uri) {
        val tempat = findViewById<LinearLayout>(R.id.tempat)
        tempat.removeAllViews()

        val doc = DocumentFile.fromTreeUri(this, uri) ?: return
        val files = doc.listFiles()

        for (file in files) {
            val item = LayoutInflater.from(this)
                .inflate(R.layout.item_text, tempat, false)

            val text = item.findViewById<TextView>(R.id.huruf)
            text.text = file.name

            text.setOnClickListener {
                if (file.isDirectory) {
                    TampilFolder(file.uri)
                } else {
                    BukaFile(file.uri)
                }
            }

            tempat.addView(item)
        }
    }
}
