package rosh.myrosh.pop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import rosh.myrosh.R

class Izin(private val kelas: Activity) {

    companion object {
        private const val REQ_LOKASI = 1001
        private const val REQ_KAMERA = 1002
        private const val REQ_MEMO = 1003
        private const val REQ_NOTIFIKASI = 1004
    }

    fun Tampil() {
        val item = LayoutInflater.from(kelas).inflate(R.layout.item_izin, null)
        val log = AlertDialog.Builder(kelas).setView(item).create()
        val tutup = item.findViewById<TextView>(R.id.tutup)
        val memo = item.findViewById<LinearLayout>(R.id.memo)
        val lokasi = item.findViewById<LinearLayout>(R.id.lokasi)
        val kamera = item.findViewById<LinearLayout>(R.id.kamera)
        val jaringan = item.findViewById<LinearLayout>(R.id.jaringan)
        val notifikasi = item.findViewById<LinearLayout>(R.id.notifikasi)

        fun warnaIzin(aktif: Boolean): Int {
            return android.graphics.Color.parseColor(if (aktif) "#00FF00" else "#FF0000")
        }

        fun muatStatus() {
            lokasi.setBackgroundColor(warnaIzin(BacaIzinLokasi()))
            kamera.setBackgroundColor(warnaIzin(BacaIzinKamera()))
            memo.setBackgroundColor(warnaIzin(BacaIzinMemo()))
            jaringan.setBackgroundColor(warnaIzin(BacaIzinJaringan()))
            notifikasi.setBackgroundColor(warnaIzin(BacaIzinNotifikasi()))
        }

        muatStatus()

        lokasi.setOnClickListener {
            if (BacaIzinLokasi()) {
                Toast.makeText(kelas, "Izin Lokasi sudah diberikan", Toast.LENGTH_SHORT).show()
            } else {
                IzinLokasi()
            }
        }

        kamera.setOnClickListener {
            if (BacaIzinKamera()) {
                Toast.makeText(kelas, "Izin Kamera sudah diberikan", Toast.LENGTH_SHORT).show()
            } else {
                IzinKamera()
            }
        }

        memo.setOnClickListener {
            if (BacaIzinMemo()) {
                Toast.makeText(kelas, "Izin Penyimpanan sudah diberikan", Toast.LENGTH_SHORT).show()
            } else {
                IzinMemo()
            }
        }

        jaringan.setOnClickListener {
            if (BacaIzinJaringan()) {
                Toast.makeText(kelas, "Izin Jaringan sudah diberikan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    kelas,
                    "Izin Jaringan adalah izin normal dan diberikan saat aplikasi dipasang",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        notifikasi.setOnClickListener {
            if (BacaIzinNotifikasi()) {
                Toast.makeText(kelas, "Izin Notifikasi sudah diberikan", Toast.LENGTH_SHORT).show()
            } else {
                IzinNotifikasi()
            }
        }

        tutup.setOnClickListener { log.dismiss() }
        log.setOnShowListener { muatStatus() }
        log.show()
    }

    private fun BacaIzinLokasi(): Boolean {
        val halus = ContextCompat.checkSelfPermission(
            kelas,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val kasar = ContextCompat.checkSelfPermission(
            kelas,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return halus || kasar
    }

    private fun BacaIzinKamera(): Boolean {
        return ContextCompat.checkSelfPermission(
            kelas,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun BacaIzinMemo(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val baca = ContextCompat.checkSelfPermission(
                kelas,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            val tulis = ContextCompat.checkSelfPermission(
                kelas,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            baca && tulis
        }
    }

    private fun BacaIzinJaringan(): Boolean {
        return ContextCompat.checkSelfPermission(
            kelas,
            Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun BacaIzinNotifikasi(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                kelas,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun IzinMemo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            MemoPengaturan()
        } else {
            MemoPop()
        }
    }

    private fun IzinKamera() {
        ActivityCompat.requestPermissions(
            kelas,
            arrayOf(Manifest.permission.CAMERA),
            REQ_KAMERA
        )
    }

    private fun IzinLokasi() {
        ActivityCompat.requestPermissions(
            kelas,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQ_LOKASI
        )
    }

    private fun IzinNotifikasi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                kelas,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQ_NOTIFIKASI
            )
        } else {
            Toast.makeText(
                kelas,
                "Izin Notifikasi otomatis aktif di Android versi ini",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun MemoPop() {
        ActivityCompat.requestPermissions(
            kelas,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            REQ_MEMO
        )
    }

    private fun MemoPengaturan() {
        try {
            val intent = Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${kelas.packageName}")
            )
            kelas.startActivity(intent)
        } catch (_: Exception) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            kelas.startActivity(intent)
        }
    }
}
