package ch.sdp.vibester.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.databinding.ActivityQrScanningBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

/**
 * Represent the QR scanner
 */
class QrScanningActivity : AppCompatActivity() {
    // Const
    private val requestCodeCameraPermission = 1001

    // API
    private lateinit var camera: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var binding: ActivityQrScanningBinding

    // Value
    private var scannedValue = ""
    var uidList: ArrayList<String> = ArrayList()
    val usersRepo: DataGetter = DataGetter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivityQrScanningBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val extras = intent.extras
        if (extras != null) {
            uidList = extras.get("uidList") as ArrayList<String>
        }else{
            finish()
        }

        if (ContextCompat.checkSelfPermission(
                this@QrScanningActivity, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }

        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this@QrScanningActivity, R.anim.qr_anim)
        binding.barcodeLine.startAnimation(aniSlide)
    }


    /**
     * Setup Camera & Barcode
     */
    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        camera = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    camera.start(holder)
                } catch (e: IOException) {
                    Log.e(getString(R.string.log_tag), e.stackTrace.toString())
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    camera.start(holder)
                } catch (e: IOException) {
                    Log.e(getString(R.string.log_tag), e.stackTrace.toString())
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                camera.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue
                    runOnUiThread {
                        camera.stop()
                        if (scannedValue in uidList){
                            usersRepo.updateFieldSubFieldBoolean(FireBaseAuthenticator.getCurrentUID(), true, "friends", scannedValue)
                            Toast.makeText(this@QrScanningActivity, "Congratulations, you have a new friends", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@QrScanningActivity, "Error not an existing uid", Toast.LENGTH_SHORT).show()
                        }
                        finish()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // avoid error using non initialized property
        if(::camera.isInitialized) {
            camera.stop()
        }
    }

    /*
     * This section is related to permission request
     */

    /**
     * Ask for camera permission
     */
    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@QrScanningActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission are granted restart the activity to make sure they are taken into account
                val intentNew =  Intent(this, QrScanningActivity::class.java)
                intentNew.putExtra("uidList", uidList)
                startActivity(intentNew)
                finish()
            } else {
                // Camera permission not granted, come back to previous activity
                Toast.makeText(applicationContext, "Camera Permission should be granted for that feature", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

}