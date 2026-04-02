package rosh.myrosh

import android.os.Bundle
import android.os.Environment
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import rosh.myrosh.pop.Peringatan
import java.io.File

class Gambar : AppCompatActivity() {

    private lateinit var pusat: DrawerLayout
    private lateinit var utama: ImageView
    private lateinit var grid: GridLayout
    private lateinit var namaTerpilih: TextView
    private var folderAktif: File? = null
    
    private val glideOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .placeholder(R.drawable.gambar)
        .error(R.drawable.gambar)
        .override(300, 300)
        .centerCrop()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gambar)

        PasangId()
        Tombol()
        MuatAwal()
    }

    private fun PasangId() {
        pusat = findViewById(R.id.pusat)
        utama = findViewById(R.id.utama)
        grid = findViewById(R.id.grid)
        namaTerpilih = findViewById(R.id.nama_terpilih)
    }

    private fun MuatAwal() {
        try {
            val pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val target = when {
                pictures.exists() -> pictures
                download.exists() -> download
                else -> Environment.getExternalStorageDirectory()
            }
            TampilFolder(target)
        } catch (e: Exception) {
            namaTerpilih.text = "Error akses folder"
            utama.setImageResource(R.drawable.gambar)
        }
    }

    private fun Keluar() {
        if (!isFinishing && !isDestroyed) { 
            Peringatan(this).Tampil("Meninggalkan Photo ?", "Kembali Ke Beranda ?")
        }
    }
    
    @Deprecated("Jadul")
    override fun onBackPressed() {
        if (pusat.isDrawerOpen(GravityCompat.START)) {
            pusat.closeDrawer(GravityCompat.START)
        } else {
            Keluar()
        }
    }

    private fun Tombol() {
        findViewById<ImageView>(R.id.nav).setOnClickListener {
            pusat.openDrawer(GravityCompat.START)
        }
        findViewById<ImageView>(R.id.tutup).setOnClickListener {
            pusat.closeDrawer(GravityCompat.START)
        }
        findViewById<TextView>(R.id.keluar).setOnClickListener {
            Keluar()
        }
    }

    private fun TampilFolder(folder: File) {
        folderAktif = folder
        AturGrid(folder)
    }

    private fun AturGrid(folder: File) {
        grid.removeAllViews()
      
        try {
            Glide.get(this).clearMemory()
        } catch (e: Exception) {
            
        }

        val daftar = folder
            .listFiles()
            ?.filter { it.isFile && FileGambar(it) && it.canRead() && it.length() > 0 }
            ?.sortedBy { it.name.lowercase() }
            .orEmpty()

        if (daftar.isEmpty()) {
            namaTerpilih.text = "Tidak ada gambar"
            utama.setImageResource(R.drawable.gambar)
            return
        }

        if (namaTerpilih.text.isBlank() ||
            namaTerpilih.text == "My Image" ||
            namaTerpilih.text == "Tidak ada gambar") {
            TampilGambar(daftar.first())
        }

        for (file in daftar) {
            val item = layoutInflater.inflate(R.layout.berkas_vertical, grid, false)
            val gambar = item.findViewById<ImageView>(R.id.gambar)
            val nama = item.findViewById<TextView>(R.id.nama)

            Glide.with(this)
                .load(file)
                .apply(glideOptions)
                .thumbnail(0.1f)
                .into(gambar)

            nama.text = file.name
            item.setOnClickListener {
                TampilGambar(file)
                pusat.closeDrawer(GravityCompat.START)
            }
            grid.addView(item)
        }
    }

    private fun TampilGambar(file: File) {
        try {
            if (!file.exists() || file.length() == 0L) {
                namaTerpilih.text = "File tidak valid"
                utama.setImageResource(R.drawable.gambar)
                return
            }
            
            Glide.with(this)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.gambar)
                .error(R.drawable.gambar)
                .into(utama)
            namaTerpilih.text = file.name
        } catch (e: Exception) {
            namaTerpilih.text = "Gagal memuat"
            utama.setImageResource(R.drawable.gambar)
        }
    }

    private fun FileGambar(file: File): Boolean {
        val nama = file.name.lowercase()
        return (nama.endsWith(".jpg") ||
            nama.endsWith(".jpeg") ||
            nama.endsWith(".png") ||
            nama.endsWith(".webp") ||
            nama.endsWith(".gif") ||
            nama.endsWith(".bmp")) && file.canRead() && file.length() > 0
    }
    
    override fun onDestroy() {
        super.onDestroy()
        try {
            Glide.get(this).clearMemory()
        } catch (e: Exception) {
        }
    }
}