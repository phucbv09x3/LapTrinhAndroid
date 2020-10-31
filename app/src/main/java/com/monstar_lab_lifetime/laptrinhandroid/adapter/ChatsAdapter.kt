package com.monstar_lab_lifetime.laptrinhandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.model.Chats
import de.hdodenhof.circleimageview.CircleImageView

class ChatsAdapter :
    RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {
    private var listChat: MutableList<Chats> = mutableListOf()

    companion object {
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
    }

   // var clickCall:OnClickCall?=null
    fun setList(listChat: MutableList<Chats>) {
        this.listChat = listChat
        notifyDataSetChanged()
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message = itemView.findViewById<TextView>(R.id.tv_chatLeft)
        var image = itemView.findViewById<CircleImageView>(R.id.img_chatLeft)
        // var time=itemView.findViewById<TextView>(R.id.tv_timeSendLeft)
        var xacnhan = itemView.findViewById<TextView>(R.id.tv_isSeenR)
        val imageSend=itemView.findViewById<ImageView>(R.id.img_showLeft)
        val btn=itemView.findViewById<Button>(R.id.btn_call)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        if (viewType == TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.show_chat_right, parent, false)
            return ChatViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.show_chat_left, parent, false)
            return ChatViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val list = listChat[position]
//        holder.btn.setOnClickListener {
//            clickCall?.clickCall(list,position)
//        }
        if (list.message.equals("")){
            holder.message.visibility=View.GONE
        }else{
            holder.message.text = list.message
        }
        if (list.image.toString()!=""){
            holder.imageSend.visibility=View.VISIBLE
            Glide.with(holder.imageSend).load(list.image).into(holder.imageSend)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val user = FirebaseAuth.getInstance().currentUser
        if (listChat[position].sender.equals(user?.uid)) {
            return TYPE_RIGHT
        } else {
            return TYPE_LEFT
        }

    }

}