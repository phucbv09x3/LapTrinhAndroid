package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monstar_lab_lifetime.laptrinhandroid.Interface.OnClickObjectSend
import com.monstar_lab_lifetime.laptrinhandroid.Interface.OnItemClick
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.activity.MessageActivity
import com.monstar_lab_lifetime.laptrinhandroid.adapter.MesAdapter
import com.monstar_lab_lifetime.laptrinhandroid.model.FeedData
import com.monstar_lab_lifetime.laptrinhandroid.model.MesData
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_message.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


class MessageFragment : Fragment(),CoroutineScope ,OnClickObjectSend,OnItemClick{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    var mAdapter = MesAdapter(this)
    var mList = mutableListOf<MesData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_message, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rcy_mes.layoutManager = LinearLayoutManager(activity)
        view.rcy_mes.setHasFixedSize(true)
        val myCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position:Int=viewHolder.adapterPosition
                mList.removeAt(position)
                mAdapter.notifyDataSetChanged()
            }

        }
        val itemTouchHelper = ItemTouchHelper(myCallback)
        itemTouchHelper.attachToRecyclerView(rcy_mes)
        view.rcy_mes.adapter = mAdapter
        mAdapter.setList(mList)
    }

    override fun onClick(mesData: MesData, posotion: Int) {
        val intent= Intent(context, MessageActivity::class.java)
        startActivity(intent)
    }

    override fun onClicks(feedData: FeedData, position: Int) {

    }


}
