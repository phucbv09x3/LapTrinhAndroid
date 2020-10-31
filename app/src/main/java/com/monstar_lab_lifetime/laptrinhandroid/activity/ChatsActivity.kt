package com.monstar_lab_lifetime.laptrinhandroid.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.adapter.ChatsAdapter
import com.monstar_lab_lifetime.laptrinhandroid.model.Chats
import com.sinch.android.rtc.*
import com.sinch.android.rtc.calling.Call
import com.sinch.android.rtc.calling.CallClient
import com.sinch.android.rtc.calling.CallListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_message.*

class ChatsActivity : AppCompatActivity(), View.OnClickListener {
    private var uidPeople: String = ""
    private var listChat = mutableListOf<Chats>()
    private var chatAdapter = ChatsAdapter()
    private var user: FirebaseUser? = null
    private var userRef: DatabaseReference? = null
    private var seenListener: ValueEventListener? = null
    private val REQUES_CODE = 1000
    private var uri: Uri? = null
    var call: com.sinch.android.rtc.calling.Call? = null
    private var sinch: SinchClient? = null
    private var callClient:CallClient?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        readMes()
        //seenMess()
        user = FirebaseAuth.getInstance().currentUser
        val intent = intent
        uidPeople = intent.getStringExtra("key")
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseReference = firebaseDatabase.getReference("Account")
        val query = firebaseReference.orderByChild("uid").equalTo(uidPeople)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (pos in snapshot.children) {
                    val name = pos.child("name").value.toString()
                    val imge = pos.child("img").value.toString()

                    val pathOf = findViewById<CircleImageView>(R.id.imgPeopleSend)
                    tv_PeopleSend.setText(name)
                    Glide.with(pathOf).load(imge).error(R.drawable.iconapp).into(pathOf)
                }

            }

        })
        btn_chooseImage.setOnClickListener(this)
        btn_send.setOnClickListener(this)
        val rc = findViewById<RecyclerView>(R.id.rcy_chat)
        rc.layoutManager = LinearLayoutManager(this)
        rc.setHasFixedSize(true)
        rcy_chat.adapter = chatAdapter
        chatAdapter?.setList(listChat)
        btn_call.setOnClickListener(this)
        unit()
    }
    private fun requestCallPermission() = if (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECORD_AUDIO
        )
        != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            1
        )
    } else {
        call()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun unit() {
        Log.d("checkuit", "ok")
        val context = this.applicationContext
        sinch = Sinch.getSinchClientBuilder().context(context)
            .context(this)
            .userId(user?.uid)
            .applicationKey("2c1be0ce-cd6f-40cd-ac83-ae3db5a45b3c")
            .applicationSecret("J6l/wnppz0iwPXb7j/UDqw==")
            .environmentHost("clientapi.sinch.com")
            .build()
        sinch?.setSupportCalling(true)
        sinch?.startListeningOnActiveConnection()

       // callClient = sinch!!.callClient
        //call  = callClient?.callUser(uidPeople)

        sinch?.addSinchClientListener(object : SinchClientListener {
            override fun onClientStarted(p0: SinchClient?) {
            }

            override fun onClientStopped(p0: SinchClient?) {
            }

            override fun onClientFailed(p0: SinchClient?, p1: SinchError?) {
            }

            override fun onRegistrationCredentialsRequired(
                p0: SinchClient?,
                p1: ClientRegistration?
            ) {
            }

            override fun onLogMessage(p0: Int, p1: String?, p2: String?) {
            }

        })

//
        sinch?.callClient?.addCallClientListener(object : com.sinch.android.rtc.calling.CallClientListener{
            override fun onIncomingCall(p0: CallClient?, p1: Call?) {
                Log.d("checkCall","ok")
                val dialog= AlertDialog.Builder(this@ChatsActivity)
                dialog.setTitle("Calling....")
                dialog.setPositiveButton("Từ chối") { dialog: DialogInterface?, which: Int ->
                    p1?.hangup()
                    dialog?.dismiss()
                }
                dialog.setNegativeButton("Nge") { dialog: DialogInterface?, which: Int ->
                    call=p1!!
                    call?.answer()
                    p1?.addCallListener(object : CallListener {
                        override fun onCallProgressing(p0: Call?) {

                        }

                        override fun onCallEstablished(p0: Call?) {

                        }

                        override fun onCallEnded(p0: Call?) {
                            p0?.hangup()
                        }

                        override fun onShouldSendPushNotification(
                            p0: Call?,
                            p1: MutableList<PushPair>?
                        ) {

                        }

                    })
                }
                dialog.show()
            }

        })
        sinch?.start()
    }

    private fun readMes() {
        userRef = FirebaseDatabase.getInstance().getReference("Chats")
        userRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listChat.clear()
                for (pos in snapshot.children) {
                    var chats = pos.getValue(Chats::class.java)
                    if (chats?.receive.equals(user?.uid) && chats?.sender.equals(uidPeople) ||
                        chats?.receive.equals(uidPeople) && chats?.sender.equals(user?.uid)
                    ) {
                        listChat.add(chats!!)
                        chatAdapter?.setList(listChat)
                    }
                }
            }

        })
    }
//
//    override fun onPause() {
//        super.onPause()
//        //userRef?.removeEventListener(seenListener!!)
//    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun seenMess() {
        val time = System.currentTimeMillis()
        user = FirebaseAuth.getInstance().currentUser
        val dtbRef = FirebaseDatabase.getInstance().reference
        val hashMap = HashMap<String, Any>()
        hashMap.put("sender", user!!.uid)
        hashMap.put("receive", uidPeople)
        hashMap.put("message", edt_send.text.toString())
        hashMap.put("time", time.toString())
        hashMap.put("isSeen", "xn")
        hashMap.put("image", "")
        dtbRef.child("Chats").push().setValue(hashMap)
        edt_send.setText("")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUES_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val link = data.data
            //iv.setImageURI(uriImg)
            this.uri = link

            val time = System.currentTimeMillis()
            var imgRe = FirebaseStorage.getInstance().getReference("Chat")
                .child(user?.uid.toString())
                .child("image")
            val imgname = imgRe.child("" + uri)
            imgname.putFile(uri!!)
                .addOnSuccessListener {
                    imgname.downloadUrl.addOnSuccessListener { p0 ->
                        val dtbRef = FirebaseDatabase.getInstance().reference
                        val hashMap = HashMap<String, Any>()
                        hashMap["sender"] = user!!.uid
                        hashMap["receive"] = uidPeople
                        hashMap["message"] = ""
                        hashMap["time"] = time.toString()
                        hashMap["isSeen"] = "xn"
                        hashMap["image"] = p0.toString()
                        dtbRef.child("Chats").push().setValue(hashMap)
                    }
                }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_send -> {
                val getMessage = edt_send.text.toString()
                if (TextUtils.isEmpty(getMessage)) {
                    Toast.makeText(
                        applicationContext,
                        "Bạn muốn nhắn tin không !!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    seenMess()
                }
            }
            R.id.btn_chooseImage -> {
                val intent =
                    Intent(
                        Intent.ACTION_OPEN_DOCUMENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                // intent.type = "image/*"
                startActivityForResult(intent, REQUES_CODE)
            }
            R.id.btn_call -> {
                requestCallPermission()
            }
        }
    }

    private fun call(){
        callClient = sinch?.callClient
        call = callClient?.callUser(uidPeople)
        var dialog= AlertDialog.Builder(this@ChatsActivity)
        dialog.setTitle("Calling")
        dialog.setPositiveButton("Tắt") { dialog: DialogInterface?, which: Int ->
            call?.hangup()
            dialog?.dismiss()
        }

        call?.addCallListener(object : CallListener {
            override fun onCallProgressing(p0: Call?) {
                dialog.show()
            }

            override fun onCallEstablished(p0: Call?) {

            }

            override fun onCallEnded(p0: Call?) {
                p0?.hangup()
            }

            override fun onShouldSendPushNotification(
                p0: Call?,
                p1: MutableList<PushPair>?
            ) {
            }

        })
    }

}


