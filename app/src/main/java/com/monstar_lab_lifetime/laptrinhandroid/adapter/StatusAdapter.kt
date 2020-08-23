package com.monstar_lab_lifetime.laptrinhandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.model.Status
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class StatusAdapter( var listStatus :MutableList<Status>) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {


    fun setLisst(listStatus: MutableList<Status>){
        this.listStatus=listStatus
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatusAdapter.StatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return StatusViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listStatus.size
    }

    override fun onBindViewHolder(holder: StatusAdapter.StatusViewHolder, position: Int) {
       val list=listStatus[position]
        holder.nameMy.text=list.nameMy
        holder.textContent.text=list.textAdd
        holder.textTime.text=list.time
        val imMy=list.imgMy
        val imaAdd=list.imgAdd
        Picasso.get().load(imMy).into(holder.imageMy)
        Picasso.get().load(imaAdd).into(holder.imageAdd)
    }
    class StatusViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageMy=itemView.findViewById<CircleImageView>(R.id.cv_image)
        val imageAdd=itemView.findViewById<ImageView>(R.id.iv_content)
        val nameMy=itemView.findViewById<TextView>(R.id.tv_top)
        val textContent=itemView.findViewById<TextView>(R.id.tv_content)
        val textTime=itemView.findViewById<TextView>(R.id.tv_bottom)
    }
}