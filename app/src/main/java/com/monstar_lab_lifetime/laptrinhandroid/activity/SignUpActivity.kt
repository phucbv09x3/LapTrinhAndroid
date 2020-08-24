package com.monstar_lab_lifetime.laptrinhandroid.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.database.AccountDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class SignUpActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private var mEmail =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    private var mPassword = "[a-zA-Z0-9]+[@#$%^&*]+[a-zA-A-z0-9]+"
    private lateinit var sharedPreferences: SharedPreferences
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mFirebase: DatabaseReference? = null
    private var mAccountDatabase: AccountDatabase? = null


    companion object {
        const val DATA_MAIL = "DATA_MAIL"
        const val DATA_PASS = "DATA_PASS"
        var userIds=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // mAuth = FirebaseAuth.getInstance()
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        button_singup.setOnClickListener(this)
        buttonback.setOnClickListener(this)
        hint_passwordsignup.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (hint_passwordsignup.length() >= 15) {
                    hint_passwordsignup.error = "Mật khẩu không được quá 15 kí tự"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
//       val sig=SignInActivity
//        val sharedPreferences:SharedPreferences
//        sharedPreferences = this.getSharedPreferences("test", Context.MODE_PRIVATE)
//        hint_emailsignup.setText(sharedPreferences.getString(sig.PREF_EMAIL,""))

        hint_passwordsignup.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (hint_passwordsignup.length() >= 14) {
                    hint_passwordsignup.error = "khong duoc qua 15 ki tu"
                }
            }
        })

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_singup -> {
                if (!hint_passwordsignup.text.toString()
                        .isEmpty() && hint_passwordsignup.text.toString()
                        .matches(mPassword.toRegex()) && !hint_emailsignup.text.toString().isEmpty()
                    && hint_emailsignup.text.toString().matches(
                        mEmail.toRegex()
                    )
                ) {


                    mAuth.createUserWithEmailAndPassword(
                        hint_emailsignup.text.toString(),
                        hint_passwordsignup.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->

                            if (task.isSuccessful) {


                                val user = mAuth.currentUser

                                val mail=user?.email

                               val userId = user?.uid
                                mFirebase = FirebaseDatabase.getInstance().getReference("Account")
                                val hash = HashMap<String, String>()
                                hash.put("mail", mail!!)
                                hash.put("name", hint_fullName.text.toString())
                                hash.put("img", "default")
                                hash.put("uid", userId.toString())
                                mFirebase?.child( userId!!)?.setValue(hash)

                                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                                    val data = Intent()
                                    data.putExtra(DATA_MAIL, hint_emailsignup.text.toString())
                                    data.putExtra(DATA_PASS, hint_passwordsignup.text.toString())
                                    setResult(Activity.RESULT_OK, data)
                                    finish()
                                }

                            }else{
                                Toast.makeText(
                                    this, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                            // If sign in fails, display a message to the user.




                } else {
                    if (hint_emailsignup.text.toString().isEmpty()) {
                        Toast.makeText(this, "Email không được trống ! ", Toast.LENGTH_SHORT).show()
                    } else if (!hint_emailsignup.text.toString().matches(mEmail.toRegex())) {
                        Toast.makeText(this, "Email sai cú pháp ! ", Toast.LENGTH_SHORT).show()
                    } else if (!hint_passwordsignup.text.toString().matches(mPassword.toRegex())) {
                        Toast.makeText(this, "Password sai cú pháp ! ", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                setResult(Activity.RESULT_CANCELED)
            }
            R.id.buttonback -> {
                onBackPressed()
            }
        }
    }


}
