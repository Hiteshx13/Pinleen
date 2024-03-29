package com.pinleen.mobile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.pinleen.mobile.databinding.ActivityFaceVerificationBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.ui.feature.login.LoginActivity
import com.pinleen.mobile.utils.Constants.REQUEST_CODE_CAMERA
import com.pinleen.mobile.utils.ItemClickCameraDialog
import com.pinleen.mobile.utils.showImageSelectionDialog
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FaceVerificationActivity : BaseActivity<ActivityFaceVerificationBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, FaceVerificationActivity::class.java)
        }
    }

    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> by lazy {
        ProcessCameraProvider.getInstance(this)
    }
    private lateinit var cameraExecutor: ExecutorService
    private val imageCapture: ImageCapture by lazy {
        ImageCapture.Builder().setTargetAspectRatio(RATIO_4_3).build()
    }

    val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it.let {
            val inputStream = contentResolver.openInputStream(it!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.ivProfileImage.setImageBitmap(bitmap)
            binding.llSelectPicture.visibility = View.GONE
            binding.llCameraButtons.visibility = View.VISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    override fun initListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.llButtonRotateCamera.setOnClickListener {
            if (lensFacing === CameraSelector.DEFAULT_FRONT_CAMERA) lensFacing =
                CameraSelector.DEFAULT_BACK_CAMERA else if (lensFacing === androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA) lensFacing =
                CameraSelector.DEFAULT_FRONT_CAMERA
            startCamera()
        }

        binding.llCapture.setOnClickListener {
            val bitmap = binding.viewCameraPreview.bitmap
            binding.ivProfileImage.setImageBitmap(bitmap)
            binding.llCameraOverlay.visibility = View.GONE
            binding.viewCameraPreview.visibility = View.GONE
            binding.llCameraButtons.visibility = View.VISIBLE
        }

        binding.llButtonDelete.setOnClickListener {
            binding.ivProfileImage.setImageDrawable(null)
            binding.llSelectPicture.visibility = View.VISIBLE
            binding.llCameraOverlay.visibility = View.GONE
            binding.llCameraButtons.visibility = View.GONE
        }

        binding.llButtonDone.setOnClickListener {
            launchActivity(LoginActivity.getIntent(this))
        }

        binding.btnSelectPicture.setOnClickListener {
            showImageSelectionDialog(this, true, object : ItemClickCameraDialog {

                override fun onClickGallery() {
                    contract.launch("image/*")
                }

                override fun onClickCamera() {
//                    val permissionHelper = PermissionHelper(this@FaceVerificationActivity, object : PermissionHelper.PermissionListener {
//                        override fun shouldShowRationaleInfo() {
//
//                        }
//
//                        override fun isPermissionGranted(isGranted: Boolean) {
//                            if (isGranted) {
//                                startCamera()
//                                binding.llSelectPicture.visibility = View.GONE
//                                binding.llCameraOverlay.visibility = View.VISIBLE
//                                binding.viewCameraPreview.visibility = View.VISIBLE
//
//                            } else {
//                                showRationalMessageDialog(
//                                    baseContext,
//                                    getString(R.string.please_grant_all_required_permission_from_application_settings),
//                                    object : ItemClickListener {
//                                        override fun onClick() {
//                                            val intent =
//                                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                            val uri = Uri.fromParts("package", packageName, null)
//                                            intent.data = uri
//                                            startActivity(intent)
//                                        }
//                                    }
//                                )
//                            }
//                        }
//                    })
//
//                    permissionHelper.checkForPermissions(Manifest.permission.READ_CONTACTS)


                }
            })
        }
    }


    private fun startCamera() {
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewCameraPreview.surfaceProvider)
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, lensFacing, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e("Exception", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }


//    private fun takePhoto() {
//        // Get a stable reference of the modifiable image capture use case
//        val imageCapture = imageCapture
//
//        // Create time stamped name and MediaStore entry.
//        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
//            .format(System.currentTimeMillis())
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
//            }
//        }
//
//        // Create output options object which contains file + metadata
//        val outputOptions = ImageCapture.OutputFileOptions
//            .Builder(
//                contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                contentValues
//            )
//            .build()
//
//        // Set up image capture listener, which is triggered after photo has
//        // been taken
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
//                }
//
//                override fun
//                        onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val msg = "Photo capture succeeded: ${output.savedUri}"
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                    binding.ivProfileImage.setImageURI(output.savedUri)
//                    updateUIForImageCapture(true)
//                    Log.d(TAG, msg)
//                }
//            }
//        )
//    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    startCamera()
                } else {
                    Toast.makeText(
                        this, "Allow all required permission from app settings.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityFaceVerificationBinding
        get() = ActivityFaceVerificationBinding::inflate
}


