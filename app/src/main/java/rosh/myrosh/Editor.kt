package rosh.myrosh

import android.os.*
import androidx.appcompat.app.*
import android.widget.*
import android.view.*
import android.text.*
import android.graphics.Typeface
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.*

class Editor : AppCompatActivity() {

    private lateinit var tempat: LinearLayout
    private lateinit var nav : ImageView
    private lateinit var geser : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editor)

        tempat = findViewById(R.id.tempat_editor)
        geser = findViewById(R.id.linear)
        nav = findViewById(R.id.nav)
        
        BuatEditor()
        IklanBanner()
        Tombol()
    }
    
    private fun IklanBanner() {
        val iklan = findViewById<AdView>(R.id.iklan)
        iklan.loadAd(AdRequest.Builder().build())
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
        Keluar()
    }

    private fun BuatEditor() {
        tempat.removeAllViews()
        val item = LayoutInflater.from(this).inflate(R.layout.item_editor, tempat, false)
        val nomor = item.findViewById<TextView>(R.id.nomor)
        val tulis = item.findViewById<EditText>(R.id.tulis)
        val scrol_tulis = item.findViewById<ScrollView>(R.id.scrol_tulis)
        val scrol_nomor = item.findViewById<ScrollView>(R.id.scrol_nomor)
        tempat.addView(item)
    }

    private fun Tombol(){
        nav.setOnClickListener{
            geser.visibility = if(geser.visibility == View.GONE){View.VISIBLE}else{View.GONE}
        }
    }
}