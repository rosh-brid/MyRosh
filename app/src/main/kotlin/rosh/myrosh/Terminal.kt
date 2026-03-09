package rosh.myrosh

import android.widget.*
import androidx.appcompat.app.*
import android.os.*
import android.content.*
import java.io.*
import android.graphics.Color
import android.view.LayoutInflater
import android.net.Uri
import rosh.myrosh.pop.Peringatan

class Terminal : AppCompatActivity(){

    private lateinit var mode: Switch
    private lateinit var hasil: TextView
    private lateinit var tulis: EditText

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.terminal)

        PasangId()
        Tombol()
        AturMode()
    }

    private fun Keluar(){
        val judul = "SEMUA SESI AKAN DIAKHIRI"
        val pesan = "Meninggalkan Terminal Sekarang?"
        Peringatan(this).Tampil(judul, pesan)
    }

    override fun onBackPressed(){
        Keluar()
    }

    private fun PasangId(){
        mode = findViewById(R.id.mode)
        tulis = findViewById(R.id.tulis)
        hasil = findViewById(R.id.hasil)
    }

    private fun Tombol(){
        findViewById<TextView>(R.id.kirim).setOnClickListener {
            val baca = tulis.text.toString()
            if(mode.isChecked){
                Sistem(baca)
            }else{
                Buatan(baca)
            }
            tulis.setText("")
        }

        mode.setOnCheckedChangeListener { _, _ ->
            AturMode()
        }
    }

    private fun AturMode(){
        if(mode.isChecked){
            mode.text = "Sistem"
            mode.setBackgroundColor(Color.parseColor("#fff000"))
        }else{
            mode.text = "Custom"
            mode.setBackgroundColor(Color.parseColor("#00ff00"))
        }
    }

    private fun Sistem(terima: String){
        hasil.append("${terima}\n")
    }

    private fun Buatan(terima: String){
        hasil.append("${terima}\n")
    }
}