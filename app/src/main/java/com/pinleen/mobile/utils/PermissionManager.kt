package com.pinleen.mobile.utils


import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pinleen.mobile.R
import java.lang.ref.WeakReference

class PermissionManager private constructor(private val fragment: WeakReference<AppCompatActivity>) {

    private val requiredPermissions = mutableListOf<Permission>()
    private var rationale: String? = null
    private var callback: (Boolean) -> Unit = {}
    private var detailedCallback: (Map<Permission, Boolean>) -> Unit = {}

    private val permissionCheck =
        fragment.get()?.registerForActivityResult(RequestMultiplePermissions()) { grantResults ->
            sendResultAndCleanUp(grantResults)
        }

    companion object {
        fun from(fragment: AppCompatActivity) = PermissionManager(WeakReference(fragment))
    }

    fun rationale(description: String): PermissionManager {
        rationale = description
        return this
    }

    fun request(vararg permission: Permission): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }

    fun checkPermission(callback: (Boolean) -> Unit) {
        this.callback = callback
        handlePermissionRequest()
    }

    fun checkDetailedPermission(callback: (Map<Permission, Boolean>) -> Unit) {
        this.detailedCallback = callback
        handlePermissionRequest()
    }

    private fun handlePermissionRequest() {
        fragment.get()?.let { fragment ->
            when {
                areAllPermissionsGranted(fragment) -> sendPositiveResult()
                shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
                else -> requestPermissions()
            }
        }
    }

    private fun displayRationale(fragment: AppCompatActivity) {
        showRationalMessageDialog(
            fragment,
            fragment.getString(R.string.please_grant_all_required_permission_from_application_settings),
            object : ItemClickListener {
                override fun onClick() {
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", fragment.packageName, null)
                    intent.data = uri
                    fragment.startActivity(intent)
                }
            }
        )
    }

    private fun sendPositiveResult() {
        sendResultAndCleanUp(getPermissionList().associate { it to true })
    }

    private fun sendResultAndCleanUp(grantResults: Map<String, Boolean>) {
        callback(grantResults.all { it.value })
        detailedCallback(grantResults.mapKeys { Permission.from(it.key) })
        cleanUp()
    }

    private fun cleanUp() {
        requiredPermissions.clear()
        rationale = null
        callback = {}
        detailedCallback = {}
    }

    private fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }

    private fun areAllPermissionsGranted(fragment: AppCompatActivity) =
        requiredPermissions.all { it.isGranted(fragment) }

    private fun shouldShowPermissionRationale(fragment: AppCompatActivity) =
        requiredPermissions.any { it.requiresRationale(fragment) }

    private fun getPermissionList() =
        requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()

    private fun Permission.isGranted(fragment: AppCompatActivity) =
        permissions.all { hasPermission(fragment, it) }

    private fun Permission.requiresRationale(fragment: AppCompatActivity) =
        permissions.any { fragment.shouldShowRequestPermissionRationale(it) }

    private fun hasPermission(fragment: AppCompatActivity, permission: String) =
        ContextCompat.checkSelfPermission(
            fragment.applicationContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    sealed class Permission(vararg val permissions: String) {
        // Individual permissions
        object Camera : Permission(CAMERA)

        // Bundled permissions
        object MandatoryForFeatureOne : Permission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

        // Grouped permissions
        object Location : Permission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
        object Storage : Permission(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
        object ReadContacts : Permission(READ_CONTACTS)


        companion object {
            fun from(permission: String) = when (permission) {
                ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
                WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
                CAMERA -> Camera
                READ_CONTACTS -> ReadContacts
                else -> throw IllegalArgumentException("Unknown permission: $permission")
            }
        }
    }
}