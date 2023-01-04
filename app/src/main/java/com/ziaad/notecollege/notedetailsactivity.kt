package com.ziaad.notecollege

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_creatnote.*
import kotlinx.android.synthetic.main.activity_notedetailsactivity.*

class notedetailsactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notedetailsactivity)

        setSupportActionBar(toolBarForDetail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var data=intent

        tvDetailContent.text = data.getStringExtra("content")
        tvDetailTitle.text = data.getStringExtra("title")
        fabEditNote.setOnClickListener {
            var intent= Intent(this,editnoteactivity::class.java)
            intent.putExtra("title",data.getStringExtra("title"))
            intent.putExtra("content",data.getStringExtra("content"))
            intent.putExtra("noteId",data.getStringExtra("noteId"))
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }
}