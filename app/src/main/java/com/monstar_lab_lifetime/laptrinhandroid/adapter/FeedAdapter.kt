package com.monstar_lab_lifetime.laptrinhandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.monstar_lab_lifetime.laptrinhandroid.model.FeedData
import com.monstar_lab_lifetime.laptrinhandroid.Interface.OnItemClick
import com.monstar_lab_lifetime.laptrinhandroid.R

class FeedAdapter(var feedList: MutableList<FeedData>, var onItemClick: OnItemClick) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        var feedData = feedList[position]
        holder.imageProfile.setImageResource(feedData.mImageProfile)
        holder.nameTop.text = feedData.mName
        holder.date.text = feedData.mDate
        holder.content.text = (feedData.mContent)
        holder.imageContent.setImageResource(feedData.mImageContent)
        holder.imageProfile.setOnClickListener {
            onItemClick.onClicks(feedData, position)
        }


//        holder.imageContent.setOnClickListener{
//            listener.onClickNe(feedData,position)
//        }
//        holder.heart.setImageResource(R.drawable.heart)
//        holder.heart.setOnClickListener {
//            listener.onClickNe(feedData,position)
//        }

    }


    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageProfile = itemView.findViewById(R.id.cv_image) as ImageView
        val nameTop = itemView.findViewById(R.id.tv_top) as TextView
        val date = itemView.findViewById(R.id.tv_bottom) as TextView
        val content = itemView.findViewById(R.id.tv_content) as TextView
        val imageContent = itemView.findViewById(R.id.iv_content) as ImageView

        // val heart=itemView.findViewById(R.id.iv_heart) as ImageButton
    }


}