package com.pinleen.mobile.ui.feature.dashboard.chat


import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.RowChatBinding
import com.pinleen.mobile.databinding.RowContactListBinding
import okhttp3.internal.notify
import java.util.*


class ChatAdapter(
    private val context: Context,
     val listMessage: ArrayList<ChatModel>
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(model:ChatModel){
        listMessage.add(model)
        this.notifyDataSetChanged()
    }
    class ViewHolder(view: RowChatBinding) : RecyclerView.ViewHolder(view.root) {
        val binding: RowChatBinding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<RowChatBinding>(
            inflater,
            R.layout.row_chat,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model=listMessage[position]
        holder.binding.tvMessage.text = model.message
        if(model.type==0){
            holder.binding.llParent.gravity= Gravity.END
            holder.binding.tvMessage.background= context.getDrawable(R.drawable.bg_button_green_rounded)
        }
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }
}