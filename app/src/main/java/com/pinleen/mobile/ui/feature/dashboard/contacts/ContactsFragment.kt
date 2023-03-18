package com.pinleen.mobile.ui.feature.dashboard.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.FragmentContactsBinding
import com.pinleen.mobile.ui.base.BaseFragment
import com.pinleen.mobile.utils.AppUtils.openAppSettings
import com.pinleen.mobile.utils.ItemClickListener
import com.pinleen.mobile.utils.PermissionHelper
import com.pinleen.mobile.utils.showRationalMessageDialog

class ContactsFragment : BaseFragment<FragmentContactsBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactsBinding
        get() = FragmentContactsBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getContactList()
    }

    private fun getContactList() {
        val permissionHelper = PermissionHelper(this, object : PermissionHelper.PermissionListener {
            override fun shouldShowRationaleInfo() {

            }

            override fun isPermissionGranted(isGranted: Boolean) {
                if (isGranted) {
                    getContacts()

                } else {
                    showRationalMessageDialog(
                        mActivity,
                        getString(R.string.please_grant_all_required_permission_from_application_settings),
                        object : ItemClickListener {
                            override fun onClick() {
                                openAppSettings(mActivity)
                            }
                        }
                    )
                }
            }
        })

        permissionHelper.checkForPermissions(Manifest.permission.READ_CONTACTS)



    }

    @SuppressLint("Range")
    fun getContacts() {
        val list = ArrayList<ContactModel>()
        val phones = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            Log.d("name>>", name + "  " + phoneNumber)
            list.add(ContactModel(name, phoneNumber))
        }

        if (phones != null) {
            phones.close()
        }

        val adapter = ContactsAdapter(mActivity, list)

        binding.rvContacts.layoutManager = LinearLayoutManager(mActivity)
        binding.rvContacts.adapter = adapter
    }
}