package rosh.myrosh

import android.os.*
import android.view.*
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.*

class Browser : AppCompatActivity() {

    private lateinit var web: WebView
    private lateinit var nav: ImageView
    private lateinit var geser: LinearLayout
    private lateinit var tulis: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.browser)

        PasangId()
        AturWeb()
        Tombol()
    }

    private fun PasangId() {
        web = findViewById(R.id.web)
        nav = findViewById(R.id.nav)
        geser = findViewById(R.id.geser)
        tulis = findViewById(R.id.tulis)
    }

    private fun AturWeb() {
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true

        web.webViewClient = WebViewClient()

        web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                ProsesBar(newProgress)
            }
        }

        TelusuriWeb("https://www.google.com")
    }

    private fun ProsesBar(progress: Int) {
    val proses = findViewById<FrameLayout>(R.id.proses)
    val uiProses = findViewById<LinearLayout>(R.id.ui_proses)
    val persenProses = findViewById<TextView>(R.id.persen_proses)

    proses.visibility = View.VISIBLE
    uiProses.visibility = View.VISIBLE

    uiProses.post {
        val parentWidth = (uiProses.parent as View).width
        val newWidth = parentWidth * progress / 100

        uiProses.layoutParams = uiProses.layoutParams.apply {
            width = newWidth
        }

        persenProses.text = "$progress%"
        if (progress >= 100) {
            proses.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        if (geser.visibility == View.VISIBLE) {
            geser.visibility = View.GONE
            IkonNav()
            return
        }

        if (web.canGoBack()) { web.goBack() }
        else { Keluar() }
    }

    private fun Keluar() {
        val item = LayoutInflater.from(this).inflate(R.layout.item_keluar, null)
        val dialog = AlertDialog.Builder(this).setView(item).create()
        val pesan = item.findViewById<TextView>(R.id.pesan)
        pesan.text = "Meninggalkan Browser dan Kembali Ke Beranda"

        item.findViewById<TextView>(R.id.batal).setOnClickListener {
            dialog.dismiss()
        }

        item.findViewById<TextView>(R.id.oke).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun IkonNav() {
        nav.setImageResource(
            if (geser.visibility == View.GONE){ R.drawable.nav }
            else{R.drawable.tutup}
        )
    }

    private fun Tombol() {
        nav.setOnClickListener {
            geser.visibility = if (geser.visibility == View.GONE) View.VISIBLE else View.GONE
            IkonNav()
        }

        findViewById<TextView>(R.id.cari).setOnClickListener {
            val input = tulis.text.toString().trim()
            if (input.isNotEmpty()) {
                val url =
                    if (input.startsWith("http"))
                        input
                    else
                        "https://www.google.com/search?q=$input"

                TelusuriWeb(url)
            }
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
    }

    private fun TelusuriWeb(link: String) {
        web.loadUrl(link)
        tulis.setText(link)
    }
}