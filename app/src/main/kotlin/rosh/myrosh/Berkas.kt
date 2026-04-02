package rosh.myrosh

import android.widget.*
import android.view.*
import androidx.appcompat.app.*
import androidx.core.view.GravityCompat
import android.os.*
import androidx.drawerlayout.widget.DrawerLayout
import java.io.*
import android.view.*
import rosh.myrosh.pop.Peringatan

class Berkas : AppCompatActivity (){

    private lateinit var pusat : DrawerLayout
    private lateinit var jalur : TextView

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.berkas)
        
        PasangId()
        Tombol()
        Segarkan(filesDir)
    }
    
    private fun PasangId(){
        pusat = findViewById(R.id.pusat)
        jalur = findViewById(R.id.jalur)
    }
    
    private fun Keluar(){
        val j = "Kembali Ke Beranda !!"
        val p = "Melanjutkan ?"
        Peringatan(this).Tampil(j,p)
    }
    
    private fun Segarkan(terima:File){
        jalur.text = terima.absolutePath
        AturGrid(terima)
    }
    
    override fun onBackPressed(){
        if(pusat.isDrawerOpen(GravityCompat.START)){pusat.closeDrawer(GravityCompat.START)}
        else{
            val target = jalur.text.toString()
            if(target == "/"){Keluar()}
            else{Segarkan(File(target).parentFile)}
        }
    }
    
    private fun AturGrid(terima:File){
        val grid = findViewById<GridLayout>(R.id.tempat_file)
        grid.removeAllViews()
         
        val list = terima.listFiles() ?: return
        for(file in list){
        
            val item = LayoutInflater.from(this).inflate(R.layout.berkas_vertical, grid, false)
            val gambar = item.findViewById<ImageView>(R.id.gambar)
            val nama = item.findViewById<TextView>(R.id.nama)
            val centang = item.findViewById<TextView>(R.id.centang)
            
            nama.text = file.name
            gambar.setImageResource(
                if(file.isDirectory){R.drawable.folder}
                else{R.drawable.file}
            )
            
            item.setOnClickListener{
                if(file.isDirectory){Segarkan(file)}else{KirimFile(file)}
            }
            
            item.setOnLongClickListener{
                true
            }
            
            grid.addView(item)
        }
    }
    
    private fun BuatBaru(){
        val item = LayoutInflater.from(this).inflate(R.layout.item_file_fol, null)
        val log = AlertDialog.Builder(this).setView(item).create()
        val batal = item.findViewById<TextView>(R.id.batal)
        val file = item.findViewById<TextView>(R.id.file)
        val fol = item.findViewById<TextView>(R.id.folder)
        
        batal.setOnClickListener{log.dismiss()}
        file.setOnClickListener{
            log.dismiss()
            MembuatFile(true)
        }
        
        fol.setOnClickListener{
            log.dismiss()
            MembuatFile(false)
        }
        
        log.show()
    }
    
    private fun MembuatFile(terima: Boolean) {
    val item = LayoutInflater.from(this).inflate(R.layout.item_tulis, null)
    val log = AlertDialog.Builder(this).setView(item).create()

    val j = item.findViewById<TextView>(R.id.judul)
    val t = item.findViewById<EditText>(R.id.tulis)
    val oke = item.findViewById<TextView>(R.id.oke)
    val batal = item.findViewById<TextView>(R.id.batal)

    oke.text = "Simpan"
    j.text = if (terima) "Membuat File Baru" else "Membuat Folder Baru"

    batal.setOnClickListener { log.dismiss() }

    oke.setOnClickListener {
        val nama = t.text.toString()
        val tempat = File(jalur.text.toString())

        val target = File(tempat, nama)

        if (target.exists()) {
            t.error = "Sudah ada"
            return@setOnClickListener
        }

        if (terima) {
            target.createNewFile()
        } else {
            target.mkdir()
        }

        Segarkan(tempat)
        log.dismiss()
    }

    log.show()
    }
    
    private fun Tombol(){
        findViewById<ImageView>(R.id.nav).setOnClickListener{
            pusat.openDrawer(GravityCompat.START)
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
        
        findViewById<ImageView>(R.id.sd).setOnClickListener{
            Segarkan(Environment.getExternalStorageDirectory())
            pusat.closeDrawer(GravityCompat.START)
            Toast.makeText(this, "MEMORY CARD", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<ImageView>(R.id.apk).setOnClickListener{
            Segarkan(filesDir)
            pusat.closeDrawer(GravityCompat.START)
            Toast.makeText(this, "Application Storage", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<ImageView>(R.id.baru).setOnClickListener{
            BuatBaru()
        }
        findViewById<ImageView>(R.id.cari).setOnClickListener{}
        findViewById<ImageView>(R.id.urut).setOnClickListener{}
    }
    
    private fun KirimFile(terima:File){
        val n = terima.name
        Toast.makeText(this, "${n} Belum siap", Toast.LENGTH_SHORT).show()
    }
}
