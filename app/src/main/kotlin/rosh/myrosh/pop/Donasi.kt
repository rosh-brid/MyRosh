package rosh.myrosh.pop

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import rosh.myrosh.R

class Donasi(private val kelas: Activity) {

    private var rewardedAd: RewardedAd? = null
    private val TAG = "Donasi"

    fun Tampil() {
        RewardedAd.load(
            kelas,
            "ca-app-pub-2534537144295464/6604192860",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Ad was dismissed.")
                            rewardedAd = null
                        }
                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.d(TAG, "Ad failed to show.")
                            rewardedAd = null
                        }
                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Ad showed fullscreen content.")
                        }
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    rewardedAd = null
                }
            }
        )

        val item = LayoutInflater.from(kelas).inflate(R.layout.item_donasi, null)
        val poin = item.findViewById<TextView>(R.id.poin)
        val iv = item.findViewById<TextView>(R.id.video_iklan)
        val tf = item.findViewById<TextView>(R.id.tf)

        fun muatPoin() {
            poin.text = "Poin : ${AmbilPoin()}"
        }

        iv.setOnClickListener {
            val ad = rewardedAd
            if (ad != null) {
                ad.show(kelas) { rewardItem ->
                    val total = TambahPoin(5)
                    muatPoin()
                    Toast.makeText(kelas, "Poin bertambah 5. Total $total", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(kelas, "Iklan belum siap, coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }

        tf.setOnClickListener {
            Toast.makeText(kelas, "fitur ini tidak tersedia", Toast.LENGTH_SHORT).show()
        }

        muatPoin()

        val log = AlertDialog.Builder(kelas).setView(item).create()
        val tutup = item.findViewById<TextView>(R.id.tutup)
            tutup.setOnClickListener{ log.dismiss() }
            log.show()
    }

    private fun AmbilPoin(): Int {
        return kelas.getSharedPreferences("donasi_fake", Context.MODE_PRIVATE)
            .getInt("poin", 0)
    }

    private fun TambahPoin(nilai: Int): Int {
        val total = AmbilPoin() + nilai
        kelas.getSharedPreferences("donasi_fake", Context.MODE_PRIVATE)
            .edit()
            .putInt("poin", total)
            .apply()
        return total
    }
}