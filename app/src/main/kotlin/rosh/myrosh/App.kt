package rosh.myrosh

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import rosh.myrosh.pop.*

class App : AppCompatActivity() {

    private lateinit var pusat: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)

        PasangId()
        Tombol()
        MobileAds.initialize(this)
        Iklan()
        PasangUser()
        TerimaIntent()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        TerimaIntent(intent)
    }

    private fun PasangId() {
        pusat = findViewById(R.id.pusat)
    }

    private fun Iklan() {
        val iklan = findViewById<AdView>(R.id.iklan_kotak)
        val minta = AdRequest.Builder().build()
        iklan.loadAd(minta)
    }

    private fun Keluar() {
        val pesan = "Keluar Dari Aplikasi Sekarang?"
        Peringatan(this).Tampil(null, pesan)
    }

    override fun onBackPressed() {
        if (pusat.isDrawerOpen(GravityCompat.START)) {
            pusat.closeDrawer(GravityCompat.START)
        } else {
            Keluar()
        }
    }

    private fun PasangUser() {
        val nama = findViewById<TextView>(R.id.nama)
        val pp = findViewById<ImageView>(R.id.pp)

        val data = DataLogin(this).Ambil()
        if (data != null) {
            nama.text = data.nama
            pp.setImageBitmap(data.pp)
        } else {
            nama.text = "Tamu"
            pp.setImageResource(R.drawable.pp)
        }
    }

    private fun TerimaIntent(){
        TerimaIntent(intent)
    }

    private fun TerimaIntent(target: Intent) {
        val data = AmbilUriDariIntent(target) ?: return
        DariAplikasiLain(this).Tampil(data)
    }

    @Suppress("DEPRECATION")
    private fun AmbilUriDariIntent(target: Intent): Uri? {
        return when (target.action) {
            Intent.ACTION_SEND -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    target.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    target.getParcelableExtra(Intent.EXTRA_STREAM)
                }
            }
            Intent.ACTION_VIEW -> target.data
            else -> target.data
        }
    }

    private fun Tombol() {
        findViewById<TextView>(R.id.keluar).setOnClickListener {
            Keluar()
        }

        findViewById<ImageView>(R.id.nav).setOnClickListener {
            pusat.openDrawer(GravityCompat.START)
        }

        findViewById<ImageView>(R.id.tutup).setOnClickListener {
            pusat.closeDrawer(GravityCompat.START)
        }

        findViewById<LinearLayout>(R.id.berkas).setOnClickListener {
            startActivity(Intent(this, Berkas::class.java))
        }

        findViewById<LinearLayout>(R.id.gambar).setOnClickListener {
            startActivity(Intent(this, Gambar::class.java))
        }

        findViewById<LinearLayout>(R.id.video).setOnClickListener {
            Toast.makeText(this, "Belum Siap", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.musik).setOnClickListener {
            startActivity(Intent(this, Musik::class.java))
        }

        findViewById<TextView>(R.id.izin).setOnClickListener {
            Izin(this).Tampil()
        }

        findViewById<TextView>(R.id.donasi).setOnClickListener {
            Donasi(this).Tampil()
        }

        findViewById<TextView>(R.id.profil).setOnClickListener {
            DataLogin(this).Tampil()
        }

        findViewById<TextView>(R.id.perangkat).setOnClickListener {
            InfoPerangkat(this).Tampil()
        }

        findViewById<LinearLayout>(R.id.terminal).setOnClickListener {
            startActivity(Intent(this, Terminal::class.java))
        }

        findViewById<LinearLayout>(R.id.browser).setOnClickListener {
            startActivity(Intent(this, Browser::class.java))
        }
        
        findViewById<LinearLayout>(R.id.editor).setOnClickListener {
            startActivity(Intent(this, Editor::class.java))
        }

        findViewById<LinearLayout>(R.id.obrolan).setOnClickListener {
            startActivity(Intent(this, Obrolan::class.java))
        }
    }
}
