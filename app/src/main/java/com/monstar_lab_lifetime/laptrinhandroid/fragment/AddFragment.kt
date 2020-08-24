package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_add.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AddFragment : Fragment() {

    private val REQUES_CODE = 10
    private var currenUser:FirebaseUser?=null
    var getUri: Uri?=null

    var getUrlImageMy:String=""
    var nameMy:String=""
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currenUser=FirebaseAuth.getInstance().currentUser


        val databaseReference = FirebaseDatabase.getInstance()?.getReference("Account")?.child(currenUser!!.uid)
        // var query= databaseReference!!.orderByChild("mail").equalTo(user?.email)

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error" + error.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value.toString()


                val image = snapshot.child("img").value.toString()
                tv_my.setText(name)
                this@AddFragment.getUrlImageMy=image

                this@AddFragment.nameMy=name
                val avatar = view.findViewById<CircleImageView>(R.id.cv_imageMy)
                Picasso.get().load(image).into(avatar)

                // imgtesst.setImageURI(image)

            }
        })




        btn_add.setOnClickListener {

            if (ed_add.text.toString().trim().isEmpty() && !iv_choseImage.isSelected) {
                Toast.makeText(context, "Bạn chưa chọn thông tin để đăng ", Toast.LENGTH_LONG).show()
            } else {
                if (getUri != null) {
                    var imgRe = FirebaseStorage.getInstance().getReference("Status").child(currenUser?.uid.toString())
                        .child("image")
                    val imgname = imgRe.child("" + getUri)
                    imgname.putFile(getUri!!)
                        .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                                imgname.downloadUrl.addOnSuccessListener { p0 ->
                                    val imageStore =
                                        FirebaseDatabase.getInstance().getReference("Status").child(currenUser?.uid.toString())
                                    val hashMap = HashMap<String, Any>()
                                    //
                                    //
                                    hashMap.put("img", p0.toString())
                                    hashMap.put("imageMy",getUrlImageMy)
                                    hashMap.put("nameMy",nameMy)
                                    hashMap.put("text",ed_add.text.toString())
                                    hashMap.put("uid", currenUser!!.uid)
                                    hashMap.put("time",getCurrentDate().toString())
                                    imageStore.setValue(hashMap)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Upload success..",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            ed_add.setText("")
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "no Upload ..",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }

                        })


                }

            }
        }
        btn_addImage.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // intent.type = "image/*"
            startActivityForResult(intent, REQUES_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUES_CODE && resultCode == Activity.RESULT_OK && data != null) {
            var uriImg = data.data
            iv_choseImage.setImageURI(uriImg)
            this.getUri=uriImg

        }

    }
    private fun getCurrentDate():String {
        val date = Date()
        val dateFormat = "dd/MM/yyyy hh:mm"
        val sdf = SimpleDateFormat(dateFormat)
        return sdf.format(date)
    }
}