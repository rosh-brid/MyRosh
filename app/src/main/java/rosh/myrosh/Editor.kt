package rosh.myrosh

import android.os.*
import androidx.appcompat.app.*
import android.widget.*
import android.view.*

public class Editor : AppCompatActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        
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

    
    override fun onBackPressed(){
        Keluar()
    }
}
