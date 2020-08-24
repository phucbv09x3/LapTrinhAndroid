package com.monstar_lab_lifetime.laptrinhandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.monstar_lab_lifetime.laptrinhandroid.Interface.OnClickObjectSend
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.model.MesData
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException


class MesAdapter(
    var onClickItemSend: OnClickObjectSend
) : RecyclerView.Adapter<MesAdapter.MesViewHolder>() {
    private var list: MutableList<MesData> = mutableListOf()

    private var context: Context? = null

    class MesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mNameMess = itemView.findViewById(R.id.tv_nameMes) as TextView
        var mImage = itemView.findViewById(R.id.cv_message) as CircleImageView
        var mail = itemView.findViewById(R.id.tv_bottom_name) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MesViewHolder(v)
    }

    fun setList(list: MutableList<MesData>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MesViewHolder, position: Int) {
        var list = list[position]
        holder.mNameMess.text = list.name
        holder.mail.text = list.mail
        var imageV = list.image
        if (imageV.trim().isEmpty()) {

            Picasso.get().load("http")
                .into(holder.mImage)
        } else {
            try {
                Picasso.get().load(list.image).placeholder(R.drawable.iconapp).into(holder.mImage)
            } catch (e: IOException) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
            holder.itemView.setOnClickListener {
                onClickItemSend.onClick(list, position)
            }
        }
    }
}