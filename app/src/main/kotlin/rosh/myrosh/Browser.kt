package rosh.myrosh

import android.os.*
import android.view.*
import android.content.*
import android.widget.*
import androidx.appcompat.app.*
import java.io.*
import android.util.*
import android.webkit.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import rosh.myrosh.pop.Peringatan

class Browser : AppCompatActivity() {

    private lateinit var pusat: DrawerLayout
    private lateinit var web: WebView
    private lateinit var tulis: EditText
    private lateinit var cari: ImageView
    private lateinit var proses: LinearLayout
    private lateinit var berjalan: LinearLayout
    private lateinit var mode: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.browser)

        PasangId()
        AturWeb()
        Tombol()
        AturMode()
    }

    private fun PasangId() {
        pusat = findViewById(R.id.pusat)
        web = findViewById(R.id.web)
        tulis = findViewById(R.id.tulis)
        cari = findViewById(R.id.cari)
        proses = findViewById(R.id.proses)
        berjalan = findViewById(R.id.proses_berjalan)
        mode = findViewById(R.id.mode)
    }

    private fun AturWeb(url: String = "https://google.com") {
        web.settings.javaScriptEnabled = true
        web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Proses(newProgress)
            }
        }
        web.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                tulis.setText(url)
            }
        }
        web.loadUrl(url)
    }

    private fun Proses(persen: Int) {
        proses.visibility = View.VISIBLE
        val lebar = (resources.displayMetrics.widthPixels * persen / 100)
        berjalan.layoutParams = berjalan.layoutParams.apply {
            width = lebar
        }
        if (persen == 100) { proses.visibility = View.GONE }
    }

    private fun Telusuri(baca: String) {
        val url = if (baca.startsWith("http://") || baca.startsWith("https://")) {
            baca
        } else {
            "https://www.google.com/search?q=${baca}"
        }
        web.loadUrl(url)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (pusat.isDrawerOpen(GravityCompat.START)) {
            pusat.closeDrawer(GravityCompat.START)
        } else {
            if (web.canGoBack()) { web.goBack() }
            else { Keluar() }
        }
    }

    private fun Keluar() {
        val j = "Kembali Ke Beranda"
        val p = "Melanjutkan?"
        Peringatan(this).Tampil(j, p)
    }

    private fun Tombol() {
        cari.setOnClickListener {
            val baca = tulis.text.toString()
            Telusuri(baca)
        }
        
        findViewById<ImageView>(R.id.nav).setOnClickListener{
            pusat.openDrawer(GravityCompat.START)
        }
        
        findViewById<ImageView>(R.id.tutup).setOnClickListener{
            pusat.closeDrawer(GravityCompat.START)
        }
        findViewById<ImageView>(R.id.google).setOnClickListener{
            Telusuri("https://www.google.com")
            pusat.closeDrawer(GravityCompat.START)
        }
        
        findViewById<ImageView>(R.id.duck).setOnClickListener{
            Telusuri("https://www.duckduckgo.com")
            pusat.closeDrawer(GravityCompat.START)
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
        
        mode.setOnCheckedChangeListener{_,_->
            AturMode()
        }
        
        findViewById<TextView>(R.id.unduhan).setOnClickListener{
            RiwayatUnduhan()
        }
        
        findViewById<TextView>(R.id.unduhan).setOnClickListener{
            RiwayatPenelusuran()
        }
    }
    
    private fun AturMode(){
        if(mode.isChecked){
            Toast.makeText(this, "Mode Desktop Diaktifkan",Toast.LENGTH_SHORT).show()
            mode.text = "Mode Desktop"
            web.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            web.settings.useWideViewPort = true
            web.settings.loadWithOverviewMode = true
        }else{
            Toast.makeText(this, "Mode Android Diaktifkan",Toast.LENGTH_SHORT).show()
            mode.text = "Mode Android"
            web.settings.userAgentString = null
            web.settings.useWideViewPort = false
            web.settings.loadWithOverviewMode = false
        }
        web.reload()
        pusat.closeDrawer(GravityCompat.START)
    }
    
    private fun RiwayatUnduhan(){
        Toast.makeText(this, "Belum Siap", Toast.LENGTH_SHORT).show()
    }
    
    private fun RiwayatPenelusuran(){
        Toast.makeText(this, "Belum Siap", Toast.LENGTH_SHORT).show()
    }
}