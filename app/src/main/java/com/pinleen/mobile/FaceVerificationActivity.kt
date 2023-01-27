package com.pinleen.mobile

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.pinleen.mobile.utils.*
import com.pinleen.mobile.utils.Constants.REQUEST_CODE_CAMERA
import com.pinleen.mobile.utils.PermissionManager.Permission
import com.pinleen.mobile.utils.RealPathUtil.getRealPath
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FaceVerificationActivity : BaseActivity<ActivityFaceVerificationBinding>() {


    private val permissionManager = PermissionManager.from(this)
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> by lazy {
        ProcessCameraProvider.getInstance(this)
    }
    private lateinit var cameraExecutor: ExecutorService
    private val imageCapture: ImageCapture by lazy {
        ImageCapture.Builder().setTargetAspectRatio(RATIO_4_3).build()
    }

    val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.ivProfileImage.setImageURI(Uri.parse(getRealPath(this, it!!)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initClickListener()
        cameraExecutor = Executors.newSingleThreadExecutor()

    }


    override fun initClickListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnUpload.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//            finish()
        }

        binding.llButtonRotateCamera.setOnClickListener {
            if (lensFacing === androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA) lensFacing =
                CameraSelector.DEFAULT_BACK_CAMERA else if (lensFacing === androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA) lensFacing =
                CameraSelector.DEFAULT_FRONT_CAMERA

            startCamera()
        }
        binding.llButtonDone.setOnClickListener {
            // takePhoto()

            val bitmap =
                binding.viewCameraPrevire.bitmap//getBitmapFromView(binding.viewCameraPrevire)
            binding.ivProfileImage.setImageBitmap(bitmap)
            updateUIForImageCapture(true)
        }

        binding.llButtonDelete.setOnClickListener {
            updateUIForImageCapture(false)
        }
        binding.ivOpenCamera.setOnClickListener {

            showMessageDialog(this, true, object : ItemClickCameraDialog {

                override fun onClickGallery() {
                    contract.launch("image/*")
                }

                override fun onClickCamera() {
                    permissionManager
                        .request(Permission.Camera)
                        .rationale(getString(R.string.message_camera_permission))
                        .checkDetailedPermission { result: Map<Permission, Boolean> ->
                            if (result.all { it.value }) {
                                binding.llUpload.visibility = View.GONE
                                binding.llCameraButton.visibility = View.VISIBLE
                                startCamera()
                            } else {
                                showPermissionDialog(
                                    baseContext ,
                                    getString(R.string.please_grant_all_required_permission_from_application_settings),
                                    object : ItemClickPermission {
                                        override fun onClickSettings() {
                                            val intent =
                                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            val uri = Uri.fromParts("package", packageName, null)
                                            intent.data = uri
                                            startActivity(intent)
                                        }
                                    }
                                )
                              }
                        }
                }
            })
        }
    }


    companion object {
        private const val TAG = "Pinleen"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"


    }


    private fun startCamera() {
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewCameraPrevire.surfaceProvider)
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, lensFacing, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
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

    private fun updateUIForImageCapture(isCaptured: Boolean) {

        if (isCaptured) {
            binding.viewCameraPrevire.visibility = View.INVISIBLE
            binding.llButtonRotateCamera.visibility = View.INVISIBLE
        } else {
            binding.viewCameraPrevire.visibility = View.VISIBLE
            binding.llButtonRotateCamera.visibility = View.VISIBLE
        }
    }

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
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                } else {
                    Toast.makeText(
                        this, "Allow all required permission from app settings.",
                        Toast.LENGTH_LONG
                    ).show()
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
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


