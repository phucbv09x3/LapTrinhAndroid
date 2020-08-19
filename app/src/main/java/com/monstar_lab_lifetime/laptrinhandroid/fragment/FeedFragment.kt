package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.activity.ContentActivity
import com.monstar_lab_lifetime.laptrinhandroid.adapter.FeedAdapter
import com.monstar_lab_lifetime.laptrinhandroid.model.FeedData
import java.text.SimpleDateFormat
import java.util.*


class FeedFragment : Fragment(){
    private var mFeedList :MutableList<FeedData> = mutableListOf()
   // private var type = arrayOf("Hello", "Goodbye", "Xin Chao")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        Log.d("m","okoko")
        //onItemClick=activity as OnItemClick
        var view = inflater.inflate(R.layout.fragment_feed, container, false)

        val rc = view.findViewById(R.id.rcy_feed) as RecyclerView
        rc.layoutManager = LinearLayoutManager(view.context)
        rc.setHasFixedSize(true)
        var lang = resources.getStringArray(R.array.lang)

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        var adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, lang
        )
        spinner.adapter = adapter
        Log.d("o", "ok")


        mFeedList.add(
            FeedData(
                R.drawable.ngoc,
                "Martin Palmer",
                getCurrentDate(),
                "What is the loop of Creation? How is there phuc bui something from nothing? In spite of the fact that it is impossible to prove that anythin…."
                , 0

            )
        )
        mFeedList.add(
            FeedData(
                R.drawable.mytam,
                "Daniel Leonarda",
                getCurrentDate(),
                "What is the loop of Creation? How is there something from nothing? In spite of the fact that it is impossible to prove that anythin…."
                ,
                R.drawable.real
            )
        )
        mFeedList.add(
            FeedData(
                R.drawable.billgate,
                "Raphel Nadal",
                getCurrentDate(),
                "What is the loop of Creation? How is there something from nothing? In spite of the fact that it is impossible to prove that anythin…."
                ,
                R.drawable.vn
            )
        )
        mFeedList.add(
            FeedData(
                R.drawable.fesuson,
                "Martil Phekol",
                getCurrentDate(),
                "What is the loop of Creation? How is there something from nothing? In spite of the fact that it is impossible to prove that anythin…."
                ,
                R.drawable.rectangle_copy
            )
        )
        mFeedList.add(
            FeedData(
                R.drawable.kaka,
                "Horoku Pental",
                getCurrentDate(),
                "What is the loop of Creation? How is there something from nothing? In spite of the fact that it is impossible to prove that anythin…."
                ,
                R.drawable.vn
            )
        )

        val adapte: FeedAdapter =
            FeedAdapter(
                mFeedList,
                activity as ContentActivity
            )//activity as ContentActivity
        rc.adapter = adapte
        return view
    }


    private fun getCurrentDate():String {
        val date = Date()
        val dateFormat = "dd/MM/yyyy hh:mm"
        val sdf = SimpleDateFormat(dateFormat)
        return sdf.format(date)
    }


}
