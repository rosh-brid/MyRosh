package rosh.myrosh

import android.widget.*
import android.os.*
import androidx.appcompat.app.*
import android.view.*
import java.io.*
import com.google.android.gms.ads.rewarded.*
import com.google.android.gms.ads.*

public class Berkas : AppCompatActivity(){

    private lateinit var memo : File
    private lateinit var nav : ImageView
    private lateinit var liner : LinearLayout
    private lateinit var jalur : TextView

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.berkas)
        
        memo = filesDir
        nav = findViewById(R.id.nav)
        liner = findViewById(R.id.liner)
        jalur = findViewById(R.id.jalur)
        
        IklanBanner()
        Tombol()
        Segarkan(memo)
    }
    
    private fun IkonNav(){
        nav.setImageResource(
            if(liner.visibility == View.GONE){R.drawable.nav}
            else{R.drawable.tutup}
        )
    }
    
    private fun IklanBanner() {
        val iklan = findViewById<AdView>(R.id.iklan)
        val request = AdRequest.Builder().build()
        iklan.loadAd(request)
    }
    
    override fun onBackPressed(){
        if(liner.visibility == View.VISIBLE){
            liner.visibility = View.GONE
            IkonNav()
        }else{ 
            val target = File(jalur.text.toString()).parentFile
            if(jalur.text.toString() == "/"){ Keluar() }
            else{ Segarkan(target) }
        }
    }
    
    private fun Keluar() {
        val item = LayoutInflater.from(this).inflate(R.layout.item_keluar, null)
        val pesan = item.findViewById<TextView>(R.id.pesan)
        val batal = item.findViewById<TextView>(R.id.batal)
        val oke = item.findViewById<TextView>(R.id.oke)

        pesan.text = "Anda Akan Meninggalkan Penjelajah File !! Melanjutkan?"

        val dialog = AlertDialog.Builder(this)
            .setView(item)
            .create()

        batal.setOnClickListener { dialog.dismiss() }

        oke.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }
    
    private fun Segarkan(target : File){
        jalur.text = target.absolutePath
        AturGrid(target)
    }
    
    private fun AturGrid(target: File) {
    val grid = findViewById<GridLayout>(R.id.grid)
    grid.removeAllViews()

    val list = target.listFiles() ?: return

    for (file in list) {
        val item = LayoutInflater.from(this)
            .inflate(R.layout.item_vertical, grid, false)

        val nama = item.findViewById<TextView>(R.id.nama)
        val gambar = item.findViewById<ImageView>(R.id.gambar)

        nama.text = file.name
        gambar.setImageResource(
            if (file.isDirectory) R.drawable.folder
            else R.drawable.file
        )
        
        item.setOnClickListener{
            if(file.isDirectory){ Segarkan(file) }
            else{ Toast.makeText(this, "${file.name}", Toast.LENGTH_SHORT).show()}
        }
        
        grid.addView(item)
        }
    }
    
    private fun Tombol(){
        nav.setOnClickListener{
            liner.visibility = if(liner.visibility == View.GONE){View.VISIBLE}else{View.GONE}
            IkonNav()
        }
        
        findViewById<ImageView>(R.id.sd).setOnClickListener{
            val target = Environment.getExternalStorageDirectory()
            Segarkan(target)
            liner.visibility = View.GONE
            IkonNav()
        }
        
        findViewById<ImageView>(R.id.apk_sd).setOnClickListener{
            val target = getExternalFilesDir(null)!!
            Segarkan(target)
            liner.visibility = View.GONE
            IkonNav()
        }
        
        findViewById<ImageView>(R.id.apk).setOnClickListener{
            val target = filesDir
            Segarkan(target)
            liner.visibility = View.GONE
            IkonNav()
        }
        
        findViewById<TextView>(R.id.keluar).setOnClickListener{
            Keluar()
        }
    }
    
}
