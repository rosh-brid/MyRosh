package rosh.myrosh.pop

import rosh.myrosh.R
import android.view.*
import android.os.*
import android.widget.*
import android.app.Activity
import androidx.appcompat.app.AlertDialog

class InfoPerangkat (private val kelas : Activity){

    companion object {
        init {
            try {
                System.loadLibrary("deviceinfo")
            } catch (_: Throwable) {  }
        }

        @JvmStatic
        external fun bacaCpuNative(): String
    }

    fun Tampil(){
        val item = LayoutInflater.from(kelas).inflate(R.layout.item_info_device,null)
        val log = AlertDialog.Builder(kelas).setView(item).create()
        val judul = item.findViewById<TextView>(R.id.judul)
        val pesan = item.findViewById<TextView>(R.id.pesan)
        val tutup = item.findViewById<TextView>(R.id.tutup)
        
        pesan.text = Info()
        tutup.setOnClickListener{ log.dismiss() }
        log.show()
    }
    
    private fun Info():String {
        val cpuNative = try {
            bacaCpuNative()
        } catch (_: Throwable) {
            "JNI belum aktif atau gagal dimuat"
        }

        return  buildString {
            append("Brand : ${Build.BRAND}\n")
            append("Model : ${Build.MODEL}\n")
            append("Device : ${Build.DEVICE}\n")
            append("Produk : ${Build.PRODUCT}\n")
            append("Hardware : ${Build.HARDWARE}\n")
            append("Board : ${Build.BOARD}\n")
            append("ABI : ${Build.SUPPORTED_ABIS.joinToString()}\n")
            append("Android : ${Build.VERSION.RELEASE}\n")
            append("SDK : ${Build.VERSION.SDK_INT}\n\n")
            append("CPU Native:\n")
            append(cpuNative)
        }
    }
}
