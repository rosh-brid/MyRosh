package rosh.myrosh.pop

import rosh.myrosh.R
import android.widget.TextView
import android.view.*
import androidx.appcompat.app.AlertDialog
import android.app.Activity

class Peringatan (private val kelas : Activity){
    fun Tampil(judul:String?, pesan:String){
        val item = LayoutInflater.from(kelas).inflate(R.layout.item_peringatan, null)
        val log = AlertDialog.Builder(kelas).setView(item).create()
        val judulO = item.findViewById<TextView>(R.id.judul)
        val pesanO = item.findViewById<TextView>(R.id.pesan)
        val batal = item.findViewById<TextView>(R.id.batal)
        val oke = item.findViewById<TextView>(R.id.oke)
        
        judulO.text = if(judul != null){judul}else{"Peringatan"}
        pesanO.text = pesan
        batal.setOnClickListener{ log.dismiss() }
        oke.setOnClickListener{ kelas.finish() }
        log.show()
    }
}
