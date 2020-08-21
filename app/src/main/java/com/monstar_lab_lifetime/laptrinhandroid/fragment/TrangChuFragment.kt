package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.monstar_lab_lifetime.laptrinhandroid.R
import kotlinx.android.synthetic.main.fragment_trang_chu.*

class TrangChuFragment : Fragment() {


    private lateinit var mAuth:FirebaseAuth
    private var firebaseDatabase:FirebaseDatabase?=null
    private var databaseReference:DatabaseReference?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        var user=mAuth!!.currentUser

        //Toast.makeText(context, user?.uid, Toast.LENGTH_LONG).show()



        firebaseDatabase= FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase?.getReference("Account")?.child(user!!.uid)
       // var query= databaseReference!!.orderByChild("mail").equalTo(user?.email)

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {


                val name=snapshot.child("name").value.toString()

                testText.setText(name)
            }
        })
        return inflater!!.inflate(R.layout.fragment_trang_chu, container, false)
    }


}