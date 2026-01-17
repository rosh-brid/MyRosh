package rosh.myrosh

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.*

class Browser : AppCompatActivity() {

    private lateinit var web: WebView
    private lateinit var tulis: EditText
    private lateinit var liner: LinearLayout
    private lateinit var nav: ImageView

    private lateinit var proses: FrameLayout
    private lateinit var gambar: LinearLayout
    private lateinit var persen: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.browser)

        PasangId()
        AturWeb()
        Tombol()
        AturInputTeks()
        IkonNav()
        IklanBanner()
    }
    
    private fun IklanBanner() {
        val iklan = findViewById<AdView>(R.id.iklan)
        iklan.loadAd(AdRequest.Builder().build())
    }

    private fun PasangId() {
        web = findViewById(R.id.web)
        tulis = findViewById(R.id.tulis)
        liner = findViewById(R.id.liner)
        nav = findViewById(R.id.nav)

        proses = findViewById(R.id.proses)
        gambar = findViewById(R.id.gambar_jalan)
        persen = findViewById(R.id.persen)
    }

    private fun AturWeb() {
        web.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
                setSupportZoom(true)
            }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    tulis.setText(url)
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    UpdateProgress(newProgress)
                }
            }
        }

        TelusuriWeb("https://www.google.com")
    }

    private fun TelusuriWeb(target: String) {
        val url = when {
            target.startsWith("http://") || target.startsWith("https://") -> target
            target.contains(".") -> "https://$target"
            else -> "https://www.google.com/search?q=$target"
        }
        web.loadUrl(url)
        SembunyikanKeyboard()
    }

    private fun UpdateProgress(progress: Int) {
        persen.text = "$progress%"

        proses.visibility = if (progress < 100) View.VISIBLE else View.GONE

        proses.post {
            val maxWidth = proses.width
            if (maxWidth > 0) {
                val params = gambar.layoutParams
                params.width = maxWidth * progress / 100
                gambar.layoutParams = params
            }
        }
    }

    private fun Tombol() {
        findViewById<TextView>(R.id.cari).setOnClickListener {
            val url = tulis.text.toString().trim()
            if (url.isNotEmpty()) {
                TelusuriWeb(url)
            }
        }

        nav.setOnClickListener {
            liner.visibility = if (liner.visibility == View.GONE) { View.VISIBLE } else { View.GONE }
            IkonNav()
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
        
        findViewById<ImageView>(R.id.google).setOnClickListener{
            val ganti = "https://www.google.com"
            TelusuriWeb(ganti)
            liner.visibility = View.GONE
            IkonNav()
        }
        
        findViewById<ImageView>(R.id.duck).setOnClickListener{
            val ganti = "https://www.duckduckgo.com"
            TelusuriWeb(ganti)
            liner.visibility = View.GONE
            IkonNav()
        }
        
        findViewById<TextView>(R.id.unduhan).setOnClickListener{
            Toast.makeText(this, "Belum Siap", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<TextView>(R.id.tes_jaringan).setOnClickListener{
            Toast.makeText(this, "Belum Siap", Toast.LENGTH_SHORT).show()
        }
    }

    private fun AturInputTeks() {
        tulis.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO) {
                val url = tulis.text.toString().trim()
                if (url.isNotEmpty()) { TelusuriWeb(url) }
                true
            } else { false }
        }
    }

    private fun IkonNav() {
        nav.setImageResource(
            if (liner.visibility == View.GONE)
                R.drawable.nav
            else
                R.drawable.tutup
        )
    }

    private fun SembunyikanKeyboard() {
        tulis.clearFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(tulis.windowToken, 0)
    }

    override fun onBackPressed() {
        when {
            liner.visibility == View.VISIBLE -> {
                liner.visibility = View.GONE
                IkonNav()
            }
            web.canGoBack() -> web.goBack()
            else -> Keluar()
        }
    }
    
    private fun Keluar() {
        val item = LayoutInflater.from(this).inflate(R.layout.item_keluar, null)

        val dialog = AlertDialog.Builder(this)
            .setView(item)
            .create()
        val pesan = item.findViewById<TextView>(R.id.pesan)
        pesan.text = "Anda Akan Keluar Dari Browser"

        item.findViewById<TextView>(R.id.batal).setOnClickListener {
            dialog.dismiss()
        }

        item.findViewById<TextView>(R.id.oke).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    override fun onDestroy() {
        web.destroy()
        super.onDestroy()
    }
}