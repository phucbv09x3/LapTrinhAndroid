package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.activity.SignInActivity
import kotlinx.android.synthetic.main.fragment_trang_chu.*

class TrangChuFragment : Fragment() {

    private var REQUES_CODE = 1
    private lateinit var mAuth: FirebaseAuth
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var storeReference:StorageReference?=null
    private var path:String="storage/"
    private var firebaseStore:FirebaseStorage?=null
    private var userCurrent:FirebaseUser?=null
    @RequiresApi(Build.VERSION_CODES.KITKAT)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
         userCurrent = mAuth!!.currentUser
        //Toast.makeText(context, user?.uid, Toast.LENGTH_LONG).show()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase?.getReference("Account")?.child(userCurrent!!.uid)
        // var query= databaseReference!!.orderByChild("mail").equalTo(user?.email)

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {


                val name = snapshot.child("name").value.toString()
                val mail = snapshot.child("mail").value.toString()
                testText.setText(name)
                testEmail.setText(mail)
            }
        })



        return inflater!!.inflate(R.layout.fragment_trang_chu, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogout.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(context, SignInActivity::class.java))
        }
        imgtesst.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // intent.type = "image/*"
            startActivityForResult(intent,REQUES_CODE)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUES_CODE && resultCode == Activity.RESULT_OK && data != null) {
            var uriImg = data.data
            //imgtesst.setImageURI(uriImg)
            //val user = mAuth.currentUser

//            val hash = HashMap<String, String>()
//            hash.put("img", uriImg.toString())

            imgtesst.setImageURI(userCurrent!!.photoUrl)

        }
    }

}


