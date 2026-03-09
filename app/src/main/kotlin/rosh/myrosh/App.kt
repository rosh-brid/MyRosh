package rosh.myrosh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import rosh.myrosh.pop.Peringatan
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import android.widget.*
import android.content.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdView

class App : AppCompatActivity() {

    private lateinit var pusat : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)
        
        PasangId()
        Iklan()
        Tombol()
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
    
    private fun Tombol(){
        findViewById<ImageView>(R.id.nav).setOnClickListener{
            pusat.openDrawer(GravityCompat.START)
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
        
        findViewById<LinearLayout>(R.id.izin).setOnClickListener{
        
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
        
        findViewById<LinearLayout>(R.id.berkas).setOnClickListener{
        
        }
    }
}