package rosh.myrosh

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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

        web = findViewById(R.id.web)
        tulis = findViewById(R.id.tulis)
        liner = findViewById(R.id.liner)
        nav = findViewById(R.id.nav)

        proses = findViewById(R.id.proses)
        gambar = findViewById(R.id.gambar_jalan)
        persen = findViewById(R.id.persen)

        AturWeb()
        Tombol()
        IkonNav()
    }

    private fun AturWeb() {
        web.settings.javaScriptEnabled = true

        web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                UpdateProgress(newProgress)
            }
        }

        TelusuriWeb("https://www.google.com")
    }

    private fun TelusuriWeb(target: String) {
        if (!target.startsWith("http")) {
            web.loadUrl("https://$target")
        } else {
            web.loadUrl(target)
        }
    }

    private fun UpdateProgress(progress: Int) {
        persen.text = "$progress%"

        proses.visibility = if (progress < 100) View.VISIBLE else View.GONE

        proses.post {
            val maxWidth = proses.width
            val params = gambar.layoutParams
            params.width = maxWidth * progress / 100
            gambar.layoutParams = params
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
            liner.visibility =
                if (liner.visibility == View.GONE) View.VISIBLE else View.GONE
            IkonNav()
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

    override fun onBackPressed() {
        when {
            liner.visibility == View.VISIBLE -> {
                liner.visibility = View.GONE
                IkonNav()
            }
            web.canGoBack() -> web.goBack()
            else -> super.onBackPressed()
        }
    }
}