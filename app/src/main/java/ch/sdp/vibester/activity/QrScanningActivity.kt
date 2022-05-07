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
import java.io.Serializable

/**
 * Represent the QR scanner
 */
class QrScanningActivity : AppCompatActivity() {
    private val requestCodeCameraPermission = 1001

    private lateinit var camera: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var binding: ActivityQrScanningBinding

    private var scannedValue = ""
    private var isTest: Boolean = false
    var uidList: ArrayList<String> = ArrayList()
    val usersRepo: DataGetter = DataGetter()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityQrScanningBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val extras = intent.extras
        if (extras != null) {
            uidList = extras.get("uidList") as ArrayList<String>
            isTest = extras.getBoolean("isTest", false)
        } else {
            // If no uid end of activity
            startActivityWExtra(Intent(this@QrScanningActivity, SearchUserActivity::class.java), null, null)
        }

        permissionManager()

        val aniSlide: Animation = AnimationUtils.loadAnimation(this@QrScanningActivity, R.anim.qr_anim)
        binding.barcodeLine.startAnimation(aniSlide)

        if (isTest) {
            solveDetection(Detector.Detections<Barcode>(null, null, true))
        }
    }

    /**
     *
     */
    private fun permissionManager(){
        if (ContextCompat.checkSelfPermission(
                this@QrScanningActivity, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }
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
            override fun surfaceCreated(holder: SurfaceHolder) {
                surfaceHandler(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                surfaceHandler(holder)
            }

            @SuppressLint("MissingPermission")
            private fun surfaceHandler(holder: SurfaceHolder) {
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
                Toast.makeText(applicationContext, getString(R.string.qrScanning_scannerClosed), Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                solveDetection(detections)
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


    private fun startActivityWExtra(intent: Intent, name: String?, arg: Serializable?) {
        if (name != null && arg != null) {
            intent.putExtra(name, arg)
        }
        startActivity(intent)
        finish()
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
                startActivityWExtra(Intent(this@QrScanningActivity, QrScanningActivity::class.java), "uidList", uidList)
            } else {
                // Camera permission not granted, come back to previous activity
                Toast.makeText(applicationContext, getString(R.string.qrScanning_cameraError), Toast.LENGTH_LONG).show()
                startActivityWExtra(Intent(this@QrScanningActivity, SearchUserActivity::class.java), null, null)
            }
        }
    }

    /*
     * This section is related to the handler when code is detected
     */

    /**
     * Takes care of qr/barcode reading
     */
    private fun solveDetection(detections: Detector.Detections<Barcode>) {
        val barcodes = detections.detectedItems
        if (isTest || barcodes.size() == 1) {
            if (!isTest) {
                scannedValue = barcodes.valueAt(0).rawValue
            }
            runOnUiThread {
                camera.stop()
                if (isTest || scannedValue in uidList){
                    if (!isTest) {
                        usersRepo.setSubFieldValue(FireBaseAuthenticator.getCurrentUID(),"friends", scannedValue, true)
                    }
                    Toast.makeText(this@QrScanningActivity, getString(R.string.qrScanning_newFriend), Toast.LENGTH_SHORT).show()
                    startActivityWExtra(Intent(this@QrScanningActivity, SearchUserActivity::class.java), "isSuccess", true)
                } else {
                    Toast.makeText(this@QrScanningActivity, getString(R.string.qrScanning_noExistUid), Toast.LENGTH_SHORT).show()
                    startActivityWExtra(Intent(this@QrScanningActivity, SearchUserActivity::class.java), null, null)
                }
            }
        }
    }

}