package com.ziaad.notecollege

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_creatnote.*
import kotlinx.android.synthetic.main.activity_editnoteactivity.*
import kotlinx.android.synthetic.main.activity_notedetailsactivity.*

class editnoteactivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mUser: FirebaseUser? = null
    var mStore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editnoteactivity)

        setSupportActionBar(toolBarForEdit)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser
        mStore = FirebaseFirestore.getInstance()

        var data=intent
        var content=data.getStringExtra("content")
        var title=data.getStringExtra("title")
        edEditContent.setText(content)
        edEditTitle.setText(title)

        fabSaveEditNote.setOnClickListener {
            var newTitle=edEditTitle.text.toString()
            var newContent=edEditContent.text.toString()

            if(newTitle.isEmpty()||newContent.isEmpty()){
                Toast.makeText(this, "fill box", Toast.LENGTH_SHORT).show()
            }
            else{
                //save Edit note in firebase
                var documentReference =
                    mStore!!.collection("notes").document(mUser!!.uid).collection("myNotes")
                        .document(data.getStringExtra("noteId")!!)

                var note = mapOf("title" to newTitle, "content" to newContent)

                documentReference.set(note).addOnSuccessListener() {
                    Toast.makeText(this, "Note Update ", Toast.LENGTH_SHORT).show()
                    prEditActivity.visibility = View.INVISIBLE
                    finish()
                    startActivity(Intent(this, noteactivity::class.java))

                }.addOnFailureListener() {

                    Toast.makeText(this, "faild update", Toast.LENGTH_SHORT).show()
                    prEditActivity.visibility = View.INVISIBLE
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