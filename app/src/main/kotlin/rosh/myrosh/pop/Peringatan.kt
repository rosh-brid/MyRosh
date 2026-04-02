package rosh.myrosh.pop

import rosh.myrosh.R
import android.view.*
import android.app.Activity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class Peringatan (private val kelas : Activity){
    fun Tampil(judul:String?, pesan:String){
        val item = LayoutInflater.from(kelas).inflate(R.layout.item_peringatan, null)
        val log = AlertDialog.Builder(kelas).setView(item).create()
        val j = item.findViewById<TextView>(R.id.judul)
        val p = item.findViewById<TextView>(R.id.pesan)
        val batal = item.findViewById<TextView>(R.id.batal)
        val oke = item.findViewById<TextView>(R.id.oke)
        
        j.text = if(judul != null){judul}else{"Peringatan"}
        p.text = pesan
        oke.setOnClickListener{ kelas.finish() }
        batal.setOnClickListener{ log.dismiss() }
        log.show()
    }
}
