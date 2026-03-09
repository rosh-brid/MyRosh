package rosh.myrosh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import rosh.myrosh.pop.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import android.widget.*
import android.content.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdView
import android.content.pm.PackageManager
import android.net.Uri

class App : AppCompatActivity() {

    private lateinit var pusat : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)
        
        PasangId()
        Iklan()
        Tombol()
        TerimaData()
    }
    
    private fun Keluar(){
        Peringatan(this).Tampil("Keluar Aplikasi","Yakin Meninggalkan Aplikasi?")
    }
    
    private fun Iklan(){
        val tempat = findViewById<AdView>(R.id.iklan_kotak)
        val minta = AdRequest.Builder().build()
        tempat.loadAd(minta)
    }
    
    private fun PasangId(){
        pusat = findViewById(R.id.pusat)
    }
    
    override fun onBackPressed(){
        if(pusat.isDrawerOpen(GravityCompat.START)){pusat.closeDrawer(GravityCompat.START)}
        else{ Keluar() }
    }
    
    override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {

    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (grantResults.isEmpty()) return

    val diizinkan = grantResults[0] == PackageManager.PERMISSION_GRANTED

    when (requestCode) {

        100 -> {
            if (diizinkan) {
                Toast.makeText(this,"Penyimpanan diizinkan",Toast.LENGTH_SHORT).show()
            }
        }

        101 -> {
            if (diizinkan) {
                Toast.makeText(this,"Lokasi diizinkan",Toast.LENGTH_SHORT).show()
            }
        }

        102 -> {
            if (diizinkan) {
                Toast.makeText(this,"Kamera diizinkan",Toast.LENGTH_SHORT).show()
            }
        }

    }

}
    
    private fun Tombol(){
        findViewById<ImageView>(R.id.nav).setOnClickListener{
            pusat.openDrawer(GravityCompat.START)
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
        
        findViewById<LinearLayout>(R.id.izin).setOnClickListener{
            IzinAplikasi(this).tampil()
        }
        
        findViewById<LinearLayout>(R.id.profil).setOnClickListener{
        
        }
        
        findViewById<LinearLayout>(R.id.donasi).setOnClickListener{
        
        }
        
        findViewById<LinearLayout>(R.id.bantuan).setOnClickListener{
        
        }
        
        findViewById<LinearLayout>(R.id.terminal).setOnClickListener{
            startActivity(Intent(this, Terminal::class.java))
        }
        
        findViewById<LinearLayout>(R.id.editor).setOnClickListener{
            startActivity(Intent(this, Editor::class.java))
        }
        
        findViewById<LinearLayout>(R.id.berkas).setOnClickListener{
        
        }
    }
    
    private fun TerimaData(){

    val aksi = intent.action ?: return
    val tipe = intent.type

    if(aksi == Intent.ACTION_SEND){

        val teks = intent.getStringExtra(Intent.EXTRA_TEXT)

        val file = intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)

        if(teks != null){
            Toast.makeText(this,"Tulisan diterima",Toast.LENGTH_SHORT).show()
        }

        if(file != null){
            Toast.makeText(this,"File diterima",Toast.LENGTH_SHORT).show()
        }

    }

    else if(aksi == Intent.ACTION_SEND_MULTIPLE){

        val files = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)

        if(files != null){
            Toast.makeText(this,"${files.size} file diterima",Toast.LENGTH_SHORT).show()
        }

    }

    }
}