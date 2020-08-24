package com.monstar_lab_lifetime.laptrinhandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.model.Chats
import de.hdodenhof.circleimageview.CircleImageView

class ChatsAdapter :
    RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {
    private var listChat: MutableList<Chats> = mutableListOf()
    companion object{
        val TYPE_LEFT=0
        val TYPE_RIGHT=1
    }
    fun setList(listChat: MutableList<Chats>){
        this.listChat=listChat
        notifyDataSetChanged()
    }
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var  message=itemView.findViewById<TextView>(R.id.tv_chatLeft)
        var image=itemView.findViewById<CircleImageView>(R.id.img_chatLeft)
       // var time=itemView.findViewById<TextView>(R.id.tv_timeSendLeft)

        var xacnhan=itemView.findViewById<TextView>(R.id.tv_isSeen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
       if (viewType== TYPE_RIGHT){
           val view = LayoutInflater.from(parent.context).inflate(R.layout.show_chat_right, parent, false)
           return ChatViewHolder(view)
       }else{
           val view = LayoutInflater.from(parent.context).inflate(R.layout.show_chat_left, parent, false)
           return ChatViewHolder(view)
       }

    }

    override fun getItemCount(): Int {
       return listChat.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var list=listChat[position]
        holder.message.text=list.message


//        if (position==listChat.size-1){
//            if (listChat[position].isSeen){
//                holder.xacnhan.setText("Seen")
//            }else {
//                holder.xacnhan.setText("Delivered")
//            }
//
//        }
//        else{
//            holder.xacnhan.visibility=View.GONE
//        }
    }

    override fun getItemViewType(position: Int): Int {
        var user=FirebaseAuth.getInstance().currentUser
        if (listChat[position].sender.equals(user?.uid)){
            return TYPE_RIGHT
        }
        else{
            return TYPE_LEFT
        }

    }

}