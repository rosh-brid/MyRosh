package rosh.myrosh.pop

import rosh.myrosh.R
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.app.Activity
import android.widget.*
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.content.*
import android.net.Uri
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class IzinAplikasi(private val kelas: Activity) {

    fun tampil() {

        val item = LayoutInflater.from(kelas).inflate(R.layout.item_izin, null)
        val log = AlertDialog.Builder(kelas).setView(item).create()
        val tutup = item.findViewById<TextView>(R.id.tutup)
        val penyimpanan = item.findViewById<LinearLayout>(R.id.penyimpanan)
        val lokasi = item.findViewById<LinearLayout>(R.id.lokasi)
        val kamera = item.findViewById<LinearLayout>(R.id.kamera)
        val internet = item.findViewById<LinearLayout>(R.id.internet)

        penyimpanan.setBackgroundColor(
            if (cekIzinMemo()) Color.parseColor("#99ff99")
            else Color.parseColor("#ff9999")
        )
        
        lokasi.setBackgroundColor(
            if(cekIzinLokasi()) Color.parseColor("#99ff99")
            else Color.parseColor("#ff9999")
        )
        
        kamera.setBackgroundColor(
            if(cekIzinKamera()) Color.parseColor("#99ff99")
            else Color.parseColor("#ff9999")
        )
        
        internet.setBackgroundColor(
            if(cekJaringan()) Color.parseColor("#99ff99")
            else Color.parseColor("#ff9999")
        )

        penyimpanan.setOnClickListener {
            if (!cekIzinMemo()) {
                mintaIzinMemo()
                log.dismiss()
            } else { Toast.makeText(kelas,"PENYIMPANAN SUDAH DIBERIKAN",Toast.LENGTH_SHORT).show() }
        }
        
        lokasi.setOnClickListener {
            if (!cekIzinLokasi()) {
                mintaIzinLokasi()
                log.dismiss()
            } else { Toast.makeText(kelas,"LOKASI SUDAH DIBERIKAN",Toast.LENGTH_SHORT).show() }
        }
        
        kamera.setOnClickListener {
            if (!cekIzinKamera()) {
                mintaIzinKamera()
                log.dismiss()
            } else { Toast.makeText(kelas,"KAMERA SUDAH DIBERIKAN",Toast.LENGTH_SHORT).show() }
        }
        
        internet.setOnClickListener {
            if (!cekJaringan()) {
                BukaWifi()
                log.dismiss()
            } else { Toast.makeText(kelas,"INTERNET SUDAH DIBERIKAN",Toast.LENGTH_SHORT).show() }
        }

        tutup.setOnClickListener { log.dismiss() }

        log.show()
    }


    private fun cekIzinMemo(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            android.os.Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission( kelas, Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED
        }

    }


    private fun mintaIzinMemo() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:${kelas.packageName}")
            kelas.startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(
                kelas,
                arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE ),
                100 )
        }

    }
    
    private fun cekIzinLokasi(): Boolean {
        return ContextCompat.checkSelfPermission(
            kelas,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun mintaIzinLokasi() {

    ActivityCompat.requestPermissions(
        kelas,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        101
        )
    }
    
    private fun cekIzinKamera(): Boolean {

    return ContextCompat.checkSelfPermission(
        kelas,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    }
    
    private fun mintaIzinKamera() {

    ActivityCompat.requestPermissions(
        kelas,
        arrayOf(Manifest.permission.CAMERA),
        102
    )

    }
    
    private fun cekJaringan(): Boolean {

    val cm = kelas.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        val jaringan = cm.activeNetwork ?: return false
        val kemampuan = cm.getNetworkCapabilities(jaringan) ?: return false

        return kemampuan.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
               kemampuan.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
               kemampuan.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

    } else {

        val info = cm.activeNetworkInfo
        return info != null && info.isConnected

    }
    }
    
    private fun BukaWifi(){
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    kelas.startActivity(intent)
    }
}