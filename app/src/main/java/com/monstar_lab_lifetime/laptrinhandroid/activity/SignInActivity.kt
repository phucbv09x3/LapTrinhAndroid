package com.monstar_lab_lifetime.laptrinhandroid.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.acititynew.ContentsActivity
import com.monstar_lab_lifetime.laptrinhandroid.activity.SignUpActivity
import com.monstar_lab_lifetime.laptrinhandroid.database.AccountDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private var mEmail =
        "^[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"
    private var mPassword = "[a-zA-Z0-9]+[@#$%^&*]+[a-zA-A-z0-9]+"
    private val REQUEST_CODE = 0// REQUEST_CODE là một giá trị int dùng để định danh mỗi request.
    // Khi nhận được kết quả, hàm callback sẽ trả về cùng REQUEST_CODE này để ta có thể xác định và xử lý kết quả. */

    //private lateinit var sharedPreferences: SharedPreferences

    private var mAccountDatabase: AccountDatabase? = null
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val PREF_MAILS = "PREF_MAILS"
        private const val PREF_PASSS = "PREF_PASSS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        button4.setOnClickListener(this)
        button33.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
        // loadDataSignIn()
        hint_pass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (hint_pass.length() >= 15) {
                    hint_pass.error = "Khong duoc qua 15 ki tu"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }


    override fun onClick(v: View?) {

        // button4.isEnabled=false
        when (v?.id) {
            R.id.button4 -> {
                checkSignIn()
                //delay()
            }
            R.id.button33 -> {
                onBackPressed()
            }

        }


    }

    private fun checkSignIn() {
        var getpass = hint_pass.text.toString()
        var getemail = hint_email.text.toString()
        if (!getpass.isEmpty() && getpass.matches(mPassword.toRegex()) && !getemail.isEmpty() && getemail.matches(
                mEmail.toRegex()
            )
        ) {
            auth.signInWithEmailAndPassword(hint_email.text.toString(), hint_pass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }

        } else if (!getemail.matches(mEmail.toRegex())) {
            hint_email.error = "Email sai cú pháp !"
            //Toast.makeText(this, "Vui lòng nhập đây đu ", Toast.LENGTH_SHORT).show()


        } else if (!getpass.matches(mPassword.toRegex())) {
            hint_pass.error = "Password sai cú pháp!"
            //Toast.makeText(this, "Vui lòng nhập đầy đủ ", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        val currentUser = auth.currentUser
        updateUI(currentUser)
        super.onStart()

    }

    fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                //   Toast.makeText(this, "Thành công! \n Cập nhật trang cá nhân nếu chưa có !", Toast.LENGTH_LONG).show()
                val intent = Intent(this, ContentsActivity::class.java)
                // intent.putExtra("Key",currentUser.email)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please verify your email", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun clicktv(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivityForResult(intent, REQUEST_CODE)
        //startActivity(intent)
//        overridePendingTransition(
//            R.anim.slide_in_right,
//            R.anim.slide_out_left
//        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //var resultMail = data!!.getStringExtra(SignUpActivity.DATA_MAIL)
                // var resultPass = data.getStringExtra(SignUpActivity.DATA_PASS)
                //hint_email.setText(requireNotNull(data.getStringExtra(SignupActivity.DATA_MAIL),{"miss"}))
                //hint_pass.setText(requireNotNull(data.getStringExtra(SignupActivity.DATA_PASS),{" loi"}))
                hint_pass.setText(data!!.getStringExtra(SignUpActivity.DATA_PASS).toString())
                hint_email.setText(data!!.getStringExtra(SignUpActivity.DATA_MAIL).toString())
            }
        }

    }

    fun clickFogot(view: View) {
        val buider = AlertDialog.Builder(this)
        buider.setTitle("Đặt lại mật khẩu")
        val linea = LinearLayout(this)

        val edt_mail = EditText(this)
        linea.addView(edt_mail)
        buider.setView(linea)
        edt_mail.setHint("Nhập email bạn cần khôi phục mật khẩu")
        buider.setPositiveButton("Gửi", { dialog: DialogInterface?, which: Int ->
            val mAuth =
                FirebaseAuth.getInstance().sendPasswordResetEmail(edt_mail.text.toString().trim())
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            if (p0.isSuccessful) {
                                Toast.makeText(
                                    this@SignInActivity,
                                    "Send success....",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@SignInActivity,
                                    "Send failed....",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    })
        })
        buider.setNegativeButton("Không", { dialog: DialogInterface?, which: Int ->
            dialog!!.dismiss()
        })
        buider.show()
    }
}