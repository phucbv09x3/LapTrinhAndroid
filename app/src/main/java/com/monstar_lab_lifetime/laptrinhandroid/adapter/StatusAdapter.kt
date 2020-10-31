package com.monstar_lab_lifetime.laptrinhandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.monstar_lab_lifetime.laptrinhandroid.Interface.OnClickCall
import com.monstar_lab_lifetime.laptrinhandroid.Interface.OnItemClick
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.model.Status
import de.hdodenhof.circleimageview.CircleImageView

class StatusAdapter(var listStatus: MutableList<Status>) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    private var onItemClick: OnItemClick? = null
    private var clickCall: OnClickCall? = null
    fun setLisst(listStatus: MutableList<Status>) {
        this.listStatus = listStatus
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
        val list = listStatus[position]
        holder.nameMy.text = list.nameMy
        holder.textContent.text = list.textAdd
        holder.textTime.text = list.time
        Glide.with(holder.imageMy).load(list.imgMy).error(R.drawable.heart_rd).into(holder.imageMy)
        Glide.with(holder.imageAdd).load(list.imgAdd).error(R.drawable.heart_rd)
            .into(holder.imageAdd)
        holder.countHear.text = list.countHeart.toString()
        var count=list.countHeart
        val dataReference = FirebaseDatabase.getInstance().getReference("Status")
        holder.heart.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked){
                count++

                dataReference.child("${list?.uid}").child("demTym").setValue(count)
                    .addOnSuccessListener {

                    }
            }else{
                count--
                dataReference.child("${list?.uid}").child("demTym").setValue(count)
                    .addOnSuccessListener {

                    }
            }
        }

    }

    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageMy = itemView.findViewById<CircleImageView>(R.id.cv_image)
        val imageAdd = itemView.findViewById<ImageView>(R.id.iv_content)
        val nameMy = itemView.findViewById<TextView>(R.id.tv_top)
        val textContent = itemView.findViewById<TextView>(R.id.tv_content)
        val textTime = itemView.findViewById<TextView>(R.id.tv_bottom)
        var heart = itemView.findViewById<ToggleButton>(R.id.iv_heart)
        val countHear = itemView.findViewById<TextView>(R.id.tv_countHeart)
    }
}