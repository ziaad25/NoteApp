package com.ziaad.notecollege.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ziaad.notecollege.R
import com.ziaad.notecollege.forgetpassword
import com.ziaad.notecollege.noteactivity
import com.ziaad.notecollege.signup
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        var user = mAuth!!.currentUser

        if (user != null) {
            finish()
            startActivity(Intent(this, noteactivity::class.java))
        }

        btnSignup.setOnClickListener {
            startActivity(Intent(this, signup::class.java))
        }

        tvForgetPassword.setOnClickListener {
            startActivity(Intent(this, forgetpassword::class.java))
        }

        btnLogin.setOnClickListener {
            var email = edEmailLogin.text.toString().trim()
            var password = edPasswordLogin.text.toString().trim()

            if (email.isEmpty()) {
                txtInputMailLogin.error = "should enter your email"
            } else if (password.isEmpty()) {
                txtInputPasswordLogin.error = "should enter you password"
            } else {
                prMainActivity.visibility = View.VISIBLE
                //login the user
                login()
            }

        }
    }

    private fun login() {
        var email = edEmailLogin.text.toString().trim()
        var password = edPasswordLogin.text.toString().trim()

        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener() {
            if (it.isSuccessful) {
                verifyEmail()
            } else {
                prMainActivity.visibility = View.INVISIBLE
                Toast.makeText(this, "Account doesn't exists", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun verifyEmail() {
        var user = mAuth!!.currentUser
        if (user!!.isEmailVerified) {
            Toast.makeText(this, "Email Verified", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, noteactivity::class.java))
            finish()
        } else {
            prMainActivity.visibility = View.INVISIBLE
            Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show()
            mAuth!!.signOut()
        }
    }

}