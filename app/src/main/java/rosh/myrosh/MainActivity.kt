package rosh.myrosh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.*
import java.io.File
import android.content.*
import android.net.*

class MainActivity : AppCompatActivity() {

    private lateinit var liner: LinearLayout
    private lateinit var nav: ImageView
    private lateinit var dompet: File

    private var iklanVideo: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)

        liner = findViewById(R.id.liner)
        nav = findViewById(R.id.nav)
        dompet = File(getExternalFilesDir(null), "dompet.txt")

        // pastikan dompet ada
        if (!dompet.exists()) {
            dompet.writeText("0")
        }

        Tombol()
        IklanBanner()
        LoadRewarded()
    }

    /* ================== IKLAN ================== */

    private fun IklanBanner() {
        val iklan = findViewById<AdView>(R.id.iklan)
        iklan.loadAd(AdRequest.Builder().build())
    }

    private fun LoadRewarded() {
        if (!adaInternet()) {
            iklanVideo = null
            return
        }

        RewardedAd.load(
            this,
            "ca-app-pub-2534537144295464/6604192860",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    iklanVideo = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    iklanVideo = null
                }
            }
        )
    }

    private fun TontonIklan() {
        if (!adaInternet()) {
            Toast.makeText(this, "Tidak ada internet", Toast.LENGTH_SHORT).show()
            return
        }

        if (iklanVideo != null) {
            iklanVideo?.show(this) {
                tambahSaldo(3)
                Toast.makeText(this, "3 Poin ditambahkan", Toast.LENGTH_SHORT).show()
                LoadRewarded()
            }
        } else {
            Toast.makeText(this, "Iklan belum siap", Toast.LENGTH_SHORT).show()
            LoadRewarded()
        }
    }

    /* ================== SALDO ================== */

    private fun tambahSaldo(jumlah: Int) {
        val saldo = dompet.readText().toIntOrNull() ?: 0
        dompet.writeText((saldo + jumlah).toString())
    }

    private fun Saldo() {
        val saldo = dompet.readText().toIntOrNull() ?: 0

        AlertDialog.Builder(this)
            .setMessage("Saldo anda $saldo")
            .setPositiveButton("Tonton Iklan") { _, _ ->
                TontonIklan()
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    /* ================== UI ================== */

    private fun IkonNav() {
        nav.setImageResource(
            if (liner.visibility == View.GONE) R.drawable.nav
            else R.drawable.tutup
        )
    }

    private fun Keluar() {
        val item = LayoutInflater.from(this).inflate(R.layout.item_keluar, null)

        val dialog = AlertDialog.Builder(this)
            .setView(item)
            .create()
        val pesan = item.findViewById<TextView>(R.id.pesan)
        pesan.text = "Anda Akan Keluar Aplikasi"

        item.findViewById<TextView>(R.id.batal).setOnClickListener {
            dialog.dismiss()
        }

        item.findViewById<TextView>(R.id.oke).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    override fun onBackPressed() {
        if (liner.visibility == View.VISIBLE) {
            liner.visibility = View.GONE
            IkonNav()
        } else {
            Keluar()
        }
    }

    private fun Tombol() {

        findViewById<TextView>(R.id.keluar).setOnClickListener {
            Keluar()
        }

        nav.setOnClickListener {
            liner.visibility =
                if (liner.visibility == View.GONE) View.VISIBLE else View.GONE
            IkonNav()
        }

        findViewById<LinearLayout>(R.id.saldo).setOnClickListener {
            Saldo()
        }

        findViewById<LinearLayout>(R.id.berkas).setOnClickListener {
            startActivity(Intent(this, Berkas::class.java))
        }
        
        findViewById<LinearLayout>(R.id.browser).setOnClickListener {
            startActivity(Intent(this, Browser::class.java))
        }
        
        findViewById<LinearLayout>(R.id.editor).setOnClickListener {
            startActivity(Intent(this, Editor::class.java))
        }
    }

    /* ================== UTIL ================== */

    private fun adaInternet(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val net = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(net) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}