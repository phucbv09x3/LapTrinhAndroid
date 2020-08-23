package com.monstar_lab_lifetime.laptrinhandroid.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.adapter.ChatsAdapter
import com.monstar_lab_lifetime.laptrinhandroid.model.Chats
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {
    private var uidPeople:String=""
    private var listChat= mutableListOf<Chats>()
   var chatAdapter=ChatsAdapter()
    private var user:FirebaseUser?=null
    private var userRef:DatabaseReference?=null
    private var seenListener:ValueEventListener?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        readMes()
        seenMess()
        val inten=intent
         uidPeople=inten.getStringExtra("key")
        val firebaseAuth=FirebaseAuth.getInstance()
        val firebaseDatabase=FirebaseDatabase.getInstance()
        val firebaseReference=firebaseDatabase.getReference("Account")
        val query=firebaseReference.orderByChild("uid").equalTo(uidPeople)
        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (pos in snapshot.children){
                    val name=pos.child("name").value
                    val imge=pos.child("img").value
                    val pathOf=findViewById<CircleImageView>(R.id.imgPeopleSend)

                    tv_PeopleSend.setText(name.toString())
                    Picasso.get().load(imge.toString()).placeholder(R.drawable.iconapp).into(pathOf)


                }

            }

        })
        btn_send.setOnClickListener {
            val getMessage=edt_send.text.toString()
            if (TextUtils.isEmpty(getMessage)){
                Toast.makeText(applicationContext,"Bạn muốn nhắn tin không !!",Toast.LENGTH_LONG).show()
            }else{
                val time=System.currentTimeMillis()
                user=FirebaseAuth.getInstance().currentUser
                val dtbRef=FirebaseDatabase.getInstance().reference
                val hashMap=HashMap<String,Any>()
                hashMap.put("sender",user!!.uid)
                hashMap.put("receive",uidPeople)
                hashMap.put("message",getMessage)
                hashMap.put("time",time.toString())
                hashMap.put("isSeen",false)
                dtbRef.child("Chats").push().setValue(hashMap)
                edt_send.setText("")
            }
        }
        val rc = findViewById(R.id.rcy_chat) as RecyclerView
        rc.layoutManager = LinearLayoutManager(this)

        rc.setHasFixedSize(true)

        rcy_chat.adapter=chatAdapter
        chatAdapter?.setList(listChat)

    }

    fun readMes(){

        userRef=FirebaseDatabase.getInstance().getReference("Chats")
        userRef?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                listChat.clear()
                for (pos in snapshot.children){
                    var chats=pos.getValue(Chats::class.java)
                    if (chats?.receive.equals(user?.uid)&&chats?.sender.equals(uidPeople)||
                        chats?.receive.equals(uidPeople)&&chats?.sender.equals(user?.uid)){
                        listChat.add(chats!!)
                        chatAdapter?.setList(listChat)

                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        userRef?.removeEventListener(seenListener!!)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    fun seenMess(){

        userRef=FirebaseDatabase.getInstance().getReference("Chats")
        seenListener= userRef?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (pos in snapshot.children){

                    val chats=pos.getValue(Chats::class.java)
                    if (chats?.receive.equals(user?.uid)&&chats?.sender.equals(uidPeople)){
                        val hashMap= hashMapOf<String,Any>()

                        hashMap.put("isSeen",true)
                        pos.ref.updateChildren(hashMap)


                    }
                }
            }

        })!!

    }


}