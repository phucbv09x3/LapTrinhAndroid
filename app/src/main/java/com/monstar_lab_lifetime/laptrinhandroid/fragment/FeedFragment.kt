package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.app.ProgressDialog
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.adapter.StatusAdapter
import com.monstar_lab_lifetime.laptrinhandroid.model.Status
import java.text.SimpleDateFormat
import java.util.*


class FeedFragment : Fragment(){
    private var mFeedList :MutableList<Status> = mutableListOf()
   private var statusAdapter=StatusAdapter(mFeedList)
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

        spinner.adapter = adapter
        Log.d("o", "ok")
        getAll()
        rc.adapter = statusAdapter



        return view
    }


    fun getAll(){

        val pr=ProgressDialog(context)
        pr.show()
        val firebaseUser: FirebaseUser?= FirebaseAuth.getInstance().currentUser
        var dataReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Status")
       val query=dataReference.orderByChild("ketStar")
        dataReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                mFeedList.clear()
                for (pos in snapshot.children){
                    var status: Status =pos.getValue(Status::class.java)!!
                    //Toast.makeText(context,pos.child("img").toString(), Toast.LENGTH_LONG).show()
                        var obStatus= Status(pos.child("imageMy").value.toString(), pos.child("img").value.toString(),pos.child("nameMy").value.toString(),
                       pos.child("text").value.toString()
                        ,pos.child("uid").value.toString(),pos.child("time").value.toString(),pos.child("demTym").value.toString())
                      //  var po=pos.child("img").toString()
                        mFeedList.add(obStatus)

                        statusAdapter.setLisst(mFeedList)
                    pr.dismiss()

                }
            }

        })
    }
    private fun getCurrentDate():String {
        val date = Date()
        val dateFormat = "dd/MM/yyyy hh:mm"
        val sdf = SimpleDateFormat(dateFormat)
        return sdf.format(date)
    }




}
