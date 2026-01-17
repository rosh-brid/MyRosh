package rosh.myrosh

import androidx.appcompat.app.*
import android.os.*
import android.widget.*
import android.view.*
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.io.File
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.*

class Kamera : AppCompatActivity() {

    private lateinit var kamera: PreviewView
    private lateinit var ambil: ImageView
    private lateinit var galeri: ImageView
    private lateinit var balik: ImageView

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var kameraDepan = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kamera)

        kamera = findViewById(R.id.kamera)
        ambil = findViewById(R.id.ambil)
        galeri = findViewById(R.id.galeri)
        balik = findViewById(R.id.balik)

        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera()
        Tombol()
        IklanBanner()
    }
    
    private fun IklanBanner() {
        val iklan = findViewById<AdView>(R.id.iklan)
        iklan.loadAd(AdRequest.Builder().build())
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(kamera.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = if (kameraDepan) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal membuka kamera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun Tombol() {
        ambil.setOnClickListener { AmbilFoto() }
        galeri.setOnClickListener { AmbilGaleri() }
        balik.setOnClickListener { ModeKamera() }
    }

    private fun AmbilFoto() {
        val photoFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "${System.currentTimeMillis()}.jpg"
        )

        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            output,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@Kamera, "Foto tersimpan", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@Kamera, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun AmbilGaleri() {
        Toast.makeText(this, "Galeri disini", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    
    private fun ModeKamera() {
        kameraDepan = !kameraDepan
        startCamera()
    }
    
    private fun KameraDepan() {
        kameraDepan = true
        startCamera()
    }
    
    private fun KameraBelakang() {
        kameraDepan = false
        startCamera()
    }
}