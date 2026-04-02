package rosh.myrosh

import android.view.*
import android.content.*
import android.os.*
import androidx.appcompat.app.*
import android.widget.*
import android.content.*
import rosh.myrosh.pop.Peringatan
import androidx.core.view.*
import androidx.drawerlayout.widget.*

class Obrolan : AppCompatActivity (){

    private lateinit var grid:GridLayout
    private lateinit var tulis:EditText
    private lateinit var pusat: DrawerLayout

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.obrolan)
        
        PasangId()
        Tombol()
    }
    
    private fun PasangId(){
        pusat = findViewById(R.id.pusat)
        grid = findViewById(R.id.grid)
        tulis = findViewById(R.id.tulis)
    }

    private fun Keluar(){
        val j = "Kembali Ke Beranda"
        val p = "Apakah Anda Yakin ?"
        Peringatan(this).Tampil(j,p)
    }

    override fun onBackPressed(){
        if(pusat.isDrawerOpen(GravityCompat.START)){
            pusat.closeDrawer(GravityCompat.START)
        } else {
            Keluar()
        }
    }
    
    private fun Tombol(){
        findViewById<TextView>(R.id.kirim).setOnClickListener{
            val baca = tulis.text.toString()
            Kirim(baca)
            tulis.setText("")
        }
    }
    
    private fun Terima(p:String){
        val item = LayoutInflater.from(this).inflate(R.layout.item_chat_terima, grid, false)
        val pesan = item.findViewById<TextView>(R.id.pesan)
        
        pesan.text = p
        grid.addView(item)
    }
    
    private fun Kirim(p:String){
        val item = LayoutInflater.from(this).inflate(R.layout.item_chat_kirim, grid, false)
        val pesan = item.findViewById<TextView>(R.id.pesan)
        
        pesan.text = p
        grid.addView(item)
        Terima(p)
    }
}
