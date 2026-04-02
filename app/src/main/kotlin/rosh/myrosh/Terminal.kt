package rosh.myrosh

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import rosh.myrosh.pop.Peringatan
import java.io.File

class Terminal : AppCompatActivity() {

    private lateinit var tulis: EditText
    private lateinit var hasil: TextView
    private lateinit var mode: Switch
    private lateinit var pusat: DrawerLayout
    private lateinit var nama: TextView
    private lateinit var scroll: ScrollView

    private var prosesAktif: Process? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.terminal)

        PasangId()
        Tombol()
    }

    private fun PasangId() {
        tulis = findViewById(R.id.tulis)
        hasil = findViewById(R.id.hasil)
        mode = findViewById(R.id.mode)
        pusat = findViewById(R.id.pusat)
        nama = findViewById(R.id.nama)
        scroll = findViewById(R.id.scroll)
    }

    private fun Tombol() {
        findViewById<TextView>(R.id.keluar).setOnClickListener {
            Keluar()
        }

        findViewById<ImageView>(R.id.tutup).setOnClickListener {
            pusat.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.kirim).setOnClickListener {
            val baca = tulis.text.toString()
            if (baca.isNotEmpty()) {
                Mulai(baca)
                tulis.setText("")
            }
        }

        mode.setOnCheckedChangeListener { _, _ ->
            AturMode()
            pusat.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.baru).setOnClickListener {
            ProsesBaru()
            pusat.closeDrawer(GravityCompat.START)
        }
    }

    private fun Keluar() {
        val j = "Kembali ke Beranda"
        val p = "Semua Aktivitas Akan Dimatikan !!"
        Peringatan(this).Tampil(j, p)
    }

    override fun onBackPressed() {
        if (pusat.isDrawerOpen(GravityCompat.START)) {
            pusat.closeDrawer(GravityCompat.START)
        } else {
            Keluar()
        }
    }

    private fun AturMode() {
        if (mode.isChecked) {
            mode.text = "Sistem"
            mode.setBackgroundColor(Color.parseColor("#fff000"))
            Toast.makeText(this, "Sistem Digunakan", Toast.LENGTH_SHORT).show()
        } else {
            mode.text = "Custom"
            mode.setBackgroundColor(Color.parseColor("#00ff00"))
            Toast.makeText(this, "Custom Digunakan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Mulai(terima: String) {
        when (terima) {
            "clear" -> hasil.text = ""
            "exit" -> Keluar()
            else -> BacaMode(terima)
        }
    }

    private fun BacaMode(terima: String) {
        if (mode.isChecked) {
            Sistem(terima)
        } else {
            Custom(terima)
        }
    }

    private fun Sistem(terima: String) {
        ProsesBerjalan(terima)
    }

    private fun Custom(terima: String) {
        val rumah = File(filesDir, "device/home")
        val export = "export PATH=\$HOME:${rumah.absolutePath}:/system/bin:/system/xbin"
        val perintahLengkap = "$export && $terima"
        ProsesBerjalan(perintahLengkap)
    }

    private fun ProsesBaru() {
        val grid = findViewById<GridLayout>(R.id.grid)
        val item = LayoutInflater.from(this)
            .inflate(R.layout.item_terminal, grid, false)

        val angka = item.findViewById<TextView>(R.id.angka)
        val hitung = grid.childCount
        angka.text = (hitung + 1).toString()

        grid.addView(item)

        item.setOnClickListener {
            Toast.makeText(this, "Terminal ${angka.text}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ProsesBerjalan(terima: String) {
        Thread {
            try {
                val proses = ProcessBuilder("sh", "-c", terima)
                    .redirectErrorStream(true)
                    .start()

                prosesAktif = proses

                val output = proses.inputStream.bufferedReader().readText()
                proses.waitFor()

                val tampil = if (output.isNotEmpty()) output else "(tidak ada output)"

                runOnUiThread {
                    hasil.append("$ $terima\n$tampil\n")
                    AutoScroll()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    hasil.append("$ $terima\nError: ${e.message}\n")
                    AutoScroll()
                }
            }
        }.start()
    }

    private fun AutoScroll() {
        scroll.post {
            scroll.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        prosesAktif?.destroy()
    }
}