package rosh.myrosh

import android.widget.*
import android.os.*
import androidx.appcompat.app.*
import android.view.*
import android.content.*
import android.net.*

class App : AppCompatActivity(){

    private lateinit var geser : LinearLayout
    private lateinit var nav : ImageView
    private var startX = 0f
    private var lastBytes = 0L
    private var lastTime = 0L
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)
        
        PasangId()
        Tombol()
        Geser()
        MulaiKecepatanJaringan()
    }
    
    private fun PasangId(){
        geser = findViewById(R.id.geser)
        nav = findViewById(R.id.nav)
    }
    
    private fun Geser() {
    val pusat = findViewById<LinearLayout>(R.id.pusat)
    pusat.isClickable = true

    pusat.setOnTouchListener { _, event ->
        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                true
            }

            MotionEvent.ACTION_MOVE -> {
                val diffX = event.x - startX

                if (diffX > 80) {
                    geser.visibility = View.VISIBLE
                    IkonNav()
                } else if (diffX < -80) {
                    geser.visibility = View.GONE
                    IkonNav()
                }
                true
            }

            else -> false
            }
        }
    }
    
    private fun Keluar() {
        val item = LayoutInflater.from(this).inflate(R.layout.item_keluar, null)

        val dialog = AlertDialog.Builder(this)
            .setView(item)
            .create()
        val pesan = item.findViewById<TextView>(R.id.pesan)
        
        pesan.text = "Anda Akan Keluar Aplikasi"

        item.findViewById<TextView>(R.id.batal).setOnClickListener { dialog.dismiss() }

        item.findViewById<TextView>(R.id.oke).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }
    
    private fun IkonNav(){
        nav.setImageResource(
            if(geser.visibility == View.GONE){ R.drawable.nav }
            else{ R.drawable.tutup }
        )
    }
    
    override fun onBackPressed(){
        if(geser.visibility == View.VISIBLE){
            geser.visibility = View.GONE
            IkonNav()
        }else{ Keluar() }
    }
    
    private fun Tombol(){
        nav.setOnClickListener{
            geser.visibility = if(geser.visibility == View.GONE){View.VISIBLE}else{View.GONE}
            IkonNav()
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
        
        findViewById<LinearLayout>(R.id.browser).setOnClickListener{
            startActivity(Intent(this, Browser::class.java))
        }
        
        findViewById<LinearLayout>(R.id.proses_berjalan).setOnClickListener{
            ProsesBerjalan()
        }
    }
    
    private fun ProsesBerjalan(): Boolean{
        val item = LayoutInflater.from(this).inflate(R.layout.item_horizontal, null)
        val gambar = item.findViewById<ImageView>(R.id.gambar)
        val nama = item.findViewById<TextView>(R.id.nama)
        val dialog = AlertDialog.Builder(this).setView(item).create()
                    
                    
        item.findViewById<TextView>(R.id.tutup).setOnClickListener{
            Toast.makeText(this, "Belum siap", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
        return false
    }
    
    private fun MulaiKecepatanJaringan() {
        handler.post(object : Runnable {
        override fun run() {
            updateKecepatan()
            handler.postDelayed(this, 500)
            }
        })
    }

    private fun updateKecepatan() {
        val kbpsView = findViewById<TextView>(R.id.kbps)
        val nowBytes = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()
        val nowTime = System.currentTimeMillis()

            if (lastTime > 0) {
                val diffBytes = nowBytes - lastBytes
                val diffTime = (nowTime - lastTime) / 1000.0
                
                if (diffTime > 0) {
                    val kbps = (diffBytes * 8) / diffTime / 1000
                    kbpsView.text = String.format("%.2f kbps", kbps)
                }
            }

        lastBytes = nowBytes
        lastTime = nowTime
    }
    
    override fun onDestroy() {
        super.onDestroy()
            handler.removeCallbacksAndMessages(null)
    }
}