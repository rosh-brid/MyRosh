package rosh.myrosh

import androidx.appcompat.app.*
import android.os.*
import androidx.drawerlayout.widget.*
import android.widget.*
import androidx.core.view.GravityCompat
import rosh.myrosh.pop.Peringatan

class Musik : AppCompatActivity (){

    private lateinit var pusat: DrawerLayout

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.musik)

        PasangId()
    }

    private fun PasangId(){
        pusat = findViewById(R.id.pusat)
    }

    override fun onBackPressed() {
        if (pusat.isDrawerOpen(GravityCompat.START)) {
            pusat.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun Keluar(){
        val j = "Keluar"
        val p = "Apakah Anda yakin ingin keluar?"
        Peringatan(this).Tampil(j,p)
    }
}