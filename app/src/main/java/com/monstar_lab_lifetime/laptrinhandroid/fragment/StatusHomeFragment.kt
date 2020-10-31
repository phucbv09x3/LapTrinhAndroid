package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.adapter.StatusAdapter
import com.monstar_lab_lifetime.laptrinhandroid.model.Status
import com.monstar_lab_lifetime.laptrinhandroid.viewmodel.StatusHomeViewModel


class StatusHomeFragment : Fragment() {
    private var mFeedList: MutableList<Status> = mutableListOf()
    private var statusAdapter = StatusAdapter(mFeedList)
    private var statusHomeViewModel: StatusHomeViewModel? = null
    var listt: MutableList<Status> = mutableListOf<Status>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater!!.inflate(R.layout.fragment_feed, container, false)
        val rc = view.findViewById(R.id.rcy_feed) as RecyclerView
        rc.layoutManager = LinearLayoutManager(view.context)
        rc.setHasFixedSize(true)
        var lang = resources.getStringArray(R.array.lang)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        var adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, lang
        )
        statusHomeViewModel = StatusHomeViewModel()
        spinner.adapter = adapter
        getListStt()
        rc.adapter = statusAdapter

        return view

//        val map = HashMap<String, Any>()
//        map["demTym"] = 12
//        cur?.uid?.let { dataReference.child(it.toString()).updateChildren(map) }


    }


    private fun getListStt() {
        statusHomeViewModel?.getList()
        statusHomeViewModel?.listStatusLiveData?.observe(this, androidx.lifecycle.Observer {

            statusAdapter.setLisst(it)
        })
    }



}
