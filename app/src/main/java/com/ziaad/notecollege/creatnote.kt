package com.ziaad.notecollege

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_creatnote.*

class creatnote : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mUser: FirebaseUser? = null
    var mStore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creatnote)

        setSupportActionBar(toolBarForCreate)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser
        mStore = FirebaseFirestore.getInstance()

        fabSaveNote.setOnClickListener {
            var title = edCreateTitle.text.toString()
            var content = edCreateContent.text.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "there should be fill", Toast.LENGTH_SHORT).show()
            } else {
                prCreateActivity.visibility = View.VISIBLE

                //Save note in fireStore
                var documentReference =
                    mStore!!.collection("notes").document(mUser!!.uid).collection("myNotes")
                        .document()

                var note = mapOf("title" to title, "content" to content)

                documentReference.set(note).addOnSuccessListener() {
                    Toast.makeText(this, "Note created sucessfully", Toast.LENGTH_SHORT).show()
                    prCreateActivity.visibility = View.INVISIBLE
                    finish()
                    startActivity(Intent(this, noteactivity::class.java))

                }.addOnFailureListener() {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    prCreateActivity.visibility = View.INVISIBLE
                }

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }
}