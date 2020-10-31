package com.monstar_lab_lifetime.laptrinhandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.monstar_lab_lifetime.laptrinhandroid.model.Status

class StatusHomeViewModel : ViewModel() {
    var listStatusLiveData = MutableLiveData<MutableList<Status>>()
    private var listStatus = mutableListOf<Status>()
     fun getList() {
       // val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val dataReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Status")
        val quer = dataReference.limitToFirst(50)
        quer.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                listStatus.clear()
                for (pos in snapshot.children) {
                    var status: Status = pos.getValue(Status::class.java)!!
                    var obStatus = Status(
                        pos.child("imageMy").value.toString(),
                        pos.child("img").value.toString(),
                        pos.child("nameMy").value.toString(),
                        pos.child("text").value.toString(),
                        pos.child("uid").value.toString(),
                        pos.child("time").value.toString(),
                        pos.child("demTym").value.toString().toInt()
                    )
                    listStatus.add(obStatus)
                }
                listStatusLiveData.value= listStatus
            }

        })
    }


}