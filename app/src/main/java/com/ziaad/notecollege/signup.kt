package com.ziaad.notecollege

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ziaad.notecollege.ui.MainActivity
import kotlinx.android.synthetic.main.activity_signup.*

class signup : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()

        btnSignupNewUser.setOnClickListener {
            Register()

        }
        tvGotoLogin.setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun Register() {
        var email = edEmailSignup.text.toString().trim()
        var password = edPasswordSignup.text.toString().trim()
        var confirm = edConfirmPasswordSignup.text.toString().trim()

        if (email.isEmpty()) {
            txtInputMailSignup.error = "enter your mail"
        } else if (password.isEmpty() || password.length < 7) {
            txtInputPasswordSignup.error = "the password should be greater than 7"
        } else if (confirm != password) {
            txtConfirmPasswordSignup.error = "the password not same"
        } else {
            //Registr the user to firebase

            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener() {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                    sendEmailVerification()

                } else {
                    Toast.makeText(this, "Register fail", Toast.LENGTH_SHORT).show()

                }

            }
        }
    }

    private fun sendEmailVerification() {
        var user = mAuth!!.currentUser

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener() {
                if (it.isSuccessful) {
                    Toast.makeText(this,
                        "Verification Email is Sent,Verify and log In Again",
                        Toast.LENGTH_SHORT).show()
                    mAuth!!.signOut()
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}