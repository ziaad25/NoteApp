package com.ziaad.notecollege

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ziaad.notecollege.ui.MainActivity
import kotlinx.android.synthetic.main.activity_forgetpassword.*

class forgetpassword : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpassword)

        mAuth = FirebaseAuth.getInstance()

        tvBackToLogin.setOnClickListener {
            var intLogin = Intent(this, MainActivity::class.java)
            startActivity(intLogin)
            finish()
        }

        btnRecover.setOnClickListener {
            Recover()
        }
    }




    private fun Recover(){
        var email = edEmailRecover.text.toString().trim()

        if (email.isEmpty()) {
            txtInputMailRecover.error = "should enter your email"
        } else {
            //send password recover email
            mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener() {
                if (it.isSuccessful) {
                    Toast.makeText(this,
                        "Mail Sent, you can recover your password using email",
                        Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else{
                    Toast.makeText(this,
                        "Email is wrong , or Account not exits",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}