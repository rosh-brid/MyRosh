package rosh.myrosh.pop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import rosh.myrosh.Editor
import rosh.myrosh.Gambar
import rosh.myrosh.Musik
import rosh.myrosh.R
import rosh.myrosh.Terminal
import rosh.myrosh.Video

class DariAplikasiLain(private val kelas: Activity) {

    fun Tampil(terima: Uri) {
        Sesuaikan(terima)
    }

    private fun Dialog(terima: Uri) {
        val item = LayoutInflater.from(kelas).inflate(R.layout.item_kirim_terima, null)
        val builder = AlertDialog.Builder(kelas).setView(item).create()
        val dokumen = item.findViewById<View>(R.id.dokumen)
        val gambar = item.findViewById<View>(R.id.gambar)
        val video = item.findViewById<View>(R.id.video)
        val audio = item.findViewById<View>(R.id.musik)
        val terminal = item.findViewById<View>(R.id.terminal)
        val pdf = item.findViewById<View>(R.id.pdf)
        val lainnya = item.findViewById<View>(R.id.lainnya)
        val tutup = item.findViewById<View>(R.id.tutup)

        dokumen.setOnClickListener {
            builder.dismiss()
            KirimKeAnak(2, terima)
        }

        gambar.setOnClickListener {
            builder.dismiss()
            KirimKeAnak(3, terima)
        }

        video.setOnClickListener {
            builder.dismiss()
            KirimKeAnak(4, terima)
        }

        audio.setOnClickListener {
            builder.dismiss()
            KirimKeAnak(5, terima)
        }

        terminal.setOnClickListener {
            builder.dismiss()
            KirimKeAnak(1, terima)
        }

        pdf.setOnClickListener {
            builder.dismiss()
            KirimKeAnak(6, terima)
        }

        lainnya.setOnClickListener {
            builder.dismiss()
            KirimKeAnak(null, terima)
        }

        tutup.setOnClickListener {
            builder.dismiss()
        }

        builder.show()
    }

    private fun KirimKeAnak(anak: Int?, terima: Uri) {
        val tujuan = when (anak) {
            1 -> Intent(kelas, Terminal::class.java)
            2 -> Intent(kelas, Editor::class.java)
            3 -> Intent(kelas, Gambar::class.java)
            4 -> Intent(kelas, Video::class.java)
            5 -> Intent(kelas, Musik::class.java)
            6 -> Intent(kelas, Editor::class.java)
            else -> Intent(kelas, Editor::class.java)
        }

        tujuan.putExtra("terima", terima.toString())
        tujuan.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        kelas.startActivity(tujuan)
    }

    private fun Sesuaikan(terima: Uri) {
        val tipe = kelas.contentResolver.getType(terima) ?: "unknown/*"
        val item = LayoutInflater.from(kelas).inflate(R.layout.item_peringatan, null)
        val log = AlertDialog.Builder(kelas).setView(item).create()
        val judul = item.findViewById<TextView>(R.id.judul)
        val pesan = item.findViewById<TextView>(R.id.pesan)
        val batal = item.findViewById<TextView>(R.id.batal)
        val oke = item.findViewById<TextView>(R.id.oke)

        judul.text = "Dari Aplikasi Lain"
        pesan.text = "File terdeteksi dengan tipe $tipe.\nBuka langsung atau pilih aksi lain."
        batal.text = "Aksi Lain"
        oke.text = "Buka Sekarang"

        batal.setOnClickListener {
            log.dismiss()
            Dialog(terima)
        }

        oke.setOnClickListener {
            log.dismiss()
            when {
                tipe.startsWith("text") -> KirimKeAnak(2, terima)
                tipe.startsWith("image") -> KirimKeAnak(3, terima)
                tipe.startsWith("video") -> KirimKeAnak(4, terima)
                tipe.startsWith("audio") -> KirimKeAnak(5, terima)
                tipe == "application/pdf" -> KirimKeAnak(6, terima)
                else -> KirimKeAnak(1, terima)
            }
        }

        log.show()
    }
}
