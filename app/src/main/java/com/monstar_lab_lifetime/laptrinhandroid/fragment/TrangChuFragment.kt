package com.monstar_lab_lifetime.laptrinhandroid.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.acititynew.ContentsActivity
import com.monstar_lab_lifetime.laptrinhandroid.activity.SignInActivity
import com.monstar_lab_lifetime.laptrinhandroid.adapter.StatusAdapter
import com.monstar_lab_lifetime.laptrinhandroid.model.Status
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_trang_chu.*

class TrangChuFragment : Fragment() {

    private var REQUES_CODE = 1
    private lateinit var mAuth: FirebaseAuth
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var storeReference: StorageReference? = null
    private var path: String = "storage/"
    private var firebaseStore: FirebaseStorage? = null
    private var userCurrent: FirebaseUser? = null

    private val listSta= mutableListOf<Status>()
    private val statusAdap=StatusAdapter(listSta)
    @RequiresApi(Build.VERSION_CODES.KITKAT)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view=inflater!!.inflate(R.layout.fragment_trang_chu, container, false)
        val rc = view.findViewById(R.id.rc_trangchu) as RecyclerView
        rc.layoutManager = LinearLayoutManager(view.context)
        rc.setHasFixedSize(true)
        rc.adapter=statusAdap
        getAll()
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // getStatus()
        mAuth = FirebaseAuth.getInstance()
        userCurrent = mAuth?.currentUser
        //Toast.makeText(context, user?.uid, Toast.LENGTH_LONG).show()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase?.getReference("Account")?.child(userCurrent!!.uid)
        // var query= databaseReference!!.orderByChild("mail").equalTo(user?.email)

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error" + error.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value.toString()
                val mail = snapshot.child("mail").value.toString()

                val image = snapshot.child("img").value.toString()
                testText.setText(name)
                testEmail.setText(mail)

                val avatar = view.findViewById<CircleImageView>(R.id.imgtesst)
                Picasso.get().load(image).into(avatar)

                // imgtesst.setImageURI(image)

            }
        })


        btnLogout.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(context, SignInActivity::class.java))
            (context as ContentsActivity).finish()

        }
        imgtesst.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // intent.type = "image/*"
            startActivityForResult(intent, REQUES_CODE)
        }

        btnChangePass.setOnClickListener {
            change()
        }

    }
    fun  change(){
        val buider= AlertDialog.Builder(context)
        buider.setTitle("Thay đổi mật khẩu")
        val linea=LinearLayout(context)
        linea.orientation=LinearLayout.VERTICAL
        val tv_email=TextView(context)
        tv_email.setText(testEmail.text.toString())
        tv_email.setPadding(80,10,10,10)
        val edt_pass=EditText(context)

        val confim=EditText(context)
        linea.addView(tv_email)
        linea.addView(edt_pass)
        linea.addView(confim)
        buider.setView(linea)
        edt_pass.setHint("Nhập mật khẩu hiện tại")
        confim.setHint("Nhập lại mật khẩu")
        userCurrent=FirebaseAuth.getInstance().currentUser
        buider.setPositiveButton("Thay đổi",{dialog: DialogInterface?, which: Int ->
            if (!confim.text.toString().trim().equals(edt_pass.text.toString())){
                Toast.makeText(context,"Mật khẩu không khợp",Toast.LENGTH_LONG).show()
            }
            else{
                userCurrent?.let {
                    it.updatePassword(edt_pass.text.toString().trim())
                        .addOnCompleteListener{
                            task ->
                            if (task.isSuccessful){
                                Toast.makeText(context,"Thay đổi thành công !",Toast.LENGTH_LONG).show()
                            }
                        }


                }
            }
        })
        buider.setNegativeButton("Không",{
                dialog: DialogInterface?, which: Int ->
            dialog!!.dismiss()
        })
        buider.show()
    }
//    fun getStatus(){
//        var curr=FirebaseAuth.getInstance().currentUser
//        var dataReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Status").child(curr!!.uid)
//        //val query=dataReference.orderByChild("email").equalTo(firebaseUser!!.email)
//        dataReference.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (pos in snapshot.children){
//
//                    var nameMy=snapshot.child("nameMy").value.toString()
//                    var imgMy=snapshot.child("imageMy").value.toString()
//                    var img=snapshot.child("img").value.toString()
//                    var time=snapshot.child("time").value.toString()
//                    var text=snapshot.child("text").value.toString()
//                    tv_top.setText(nameMy)
//                    tv_bottom.setText(time)
//                    tv_content.setText(text)
//                    val imagMy=view!!.findViewById<CircleImageView>(R.id.cv_image)
//                    val imgContent=view!!.findViewById<ImageView>(R.id.iv_content)
//                    Picasso.get().load(imgMy).into(imagMy)
//                    Picasso.get().load(img).into(imgContent)
//                }
//            }
//
//        })
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUES_CODE && resultCode == Activity.RESULT_OK && data != null) {
            var uriImg = data.data
            //imgtesst.setImageURI(uriImg)
            //val user = mAuth.currentUser
            if (uriImg != null) {
                var imgRe = FirebaseStorage.getInstance().getReference(userCurrent?.uid.toString())
                    .child("image")
                val imgname = imgRe.child("" + uriImg)
                imgname.putFile(uriImg)
                    .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                        override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                            imgname.downloadUrl.addOnSuccessListener { p0 ->
                                val imageStore =
                                    FirebaseDatabase.getInstance().getReference("Account")
                                        .child(userCurrent?.uid.toString())
                                val hashMap = HashMap<String, Any>()
                                hashMap.put("img", p0.toString())
                                hashMap.put("mail", userCurrent!!.email.toString())
                                hashMap.put("name", testText.text.toString())
                                hashMap.put("uid", userCurrent!!.uid)
                                imageStore.setValue(hashMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Upload success..",
                                            Toast.LENGTH_LONG
                                        ).show()
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
    fun getAll(){


        val firebaseUser: FirebaseUser?= FirebaseAuth.getInstance().currentUser
        var dataReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Status")
        val query=dataReference.orderByChild("uid").equalTo(firebaseUser!!.uid)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listSta.clear()
                for (pos in snapshot.children){
                    var status: Status =pos.getValue(Status::class.java)!!
                    //Toast.makeText(context,pos.child("img").toString(), Toast.LENGTH_LONG).show()
                    var obStatus= Status(pos.child("imageMy").value.toString(), pos.child("img").value.toString(),pos.child("nameMy").value.toString(),
                        pos.child("text").value.toString()
                        ,pos.child("uid").value.toString(),pos.child("time").value.toString(),pos.child("demTym").value.toString())
                    //  var po=pos.child("img").toString()
                    listSta.add(obStatus)

                    statusAdap.setLisst(listSta)


                }
            }

        })
    }


}


