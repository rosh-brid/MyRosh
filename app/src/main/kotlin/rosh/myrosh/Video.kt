package rosh.myrosh

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import android.os.Bundle
import android.view.*
import android.widget.*

class Video : AppCompatActivity() {

    private lateinit var pusat: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video)

        PasangId()
    }

    private fun PasangId() {
        pusat = findViewById(R.id.pusat)
    }

    override fun onBackPressed() {
        if (pusat.isDrawerOpen(GravityCompat.START)) {
            pusat.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}