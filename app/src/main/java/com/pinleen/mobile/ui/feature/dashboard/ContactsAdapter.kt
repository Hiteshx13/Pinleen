package com.pinleen.mobile.ui.feature.dashboard


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.RowContactListBinding
import java.util.*


class ContactsAdapter(
    private val context: Context,
    private val contactModelArrayList: ArrayList<ContactModel>
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view: RowContactListBinding) : RecyclerView.ViewHolder(view.root) {
        val binding: RowContactListBinding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<RowContactListBinding>(
            inflater,
            R.layout.row_contact_list,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvContactName.text = contactModelArrayList[position].name
        holder.binding.tvContactNumber.text = contactModelArrayList[position].number
    }

    override fun getItemCount(): Int {
        return contactModelArrayList.size
    }
}