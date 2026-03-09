package rosh.myrosh

import android.content.*
import androidx.appcompat.app.*
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import rosh.myrosh.pop.*
import android.os.*

class Editor : AppCompatActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        
    }
    
    private fun Keluar(){
        val judul = "Meninggalkan Editor Text"
        val pesan = "Kembali ke beranda dan keluar"
        Peringatan(this).Tampil(judul, pesan)
    }
    
    override fun onBackPressed(){
        Keluar()
    }
}
