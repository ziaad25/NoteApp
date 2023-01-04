package com.ziaad.notecollege

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ziaad.notecollege.model.firebasemodel
import com.ziaad.notecollege.ui.MainActivity
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.notelayout.view.*
import kotlin.random.Random

class noteactivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mUser: FirebaseUser? = null
    var mStore: FirebaseFirestore? = null

    // var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null

    var noteAdapter: FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser
        mStore = FirebaseFirestore.getInstance()

        toolBar.inflateMenu(R.menu.menu)
        toolBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.logout) {
                mAuth!!.signOut()
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
            true
        }

        fabCreatNote.setOnClickListener {
            startActivity(Intent(this, creatnote::class.java))
        }

//for receive data from firebase
        var query = mStore!!.collection("notes").document(mUser!!.uid).collection("myNotes")
            .orderBy("title", Query.Direction.ASCENDING)
        var allUserNotes = FirestoreRecyclerOptions.Builder<firebasemodel>()
            .setQuery(query, firebasemodel::class.java).build()



        noteAdapter =
            object : FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUserNotes) {
                override fun onCreateViewHolder(parent: ViewGroup,viewType: Int,): NoteViewHolder {
                    var view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.notelayout, parent, false)
                    return NoteViewHolder(view)
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onBindViewHolder(
                    noteViewHolder: NoteViewHolder, position: Int, firebasemodel1: firebasemodel,
                ) {

                    var popButton = noteViewHolder.itemView.imMenuPopButton
                    if (firebasemodel1.title!!.isEmpty()) {
                        Toast.makeText(baseContext, "embty", Toast.LENGTH_SHORT).show()
                    }

                    //for change note's color
                    var colourCode = getRandomColor()
                    noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.resources.getColor(colourCode,null))

                    noteViewHolder.notetitle.text = firebasemodel1.title
                    noteViewHolder.notecontent.text = firebasemodel1.content

                    var docId = noteAdapter!!.snapshots.getSnapshot(position).id

                    noteViewHolder.itemView.setOnClickListener {
                    //open detail activity
                        var intent = Intent(baseContext, notedetailsactivity::class.java)

                        intent.putExtra("title", firebasemodel1.title);
                        intent.putExtra("content", firebasemodel1.content);
                        intent.putExtra("noteId", docId);

                        startActivity(intent)
                    }

                    //popButton events
                    popButton.setOnClickListener {
                        //for made image to PopMenu and add elements in
                        var popMenu = PopupMenu(baseContext, popButton)
                        popMenu.gravity = Gravity.END

                        popMenu.menu.add("Edit")
                            .setOnMenuItemClickListener {
                                var intent = Intent(baseContext, editnoteactivity::class.java)
                                intent.putExtra("title", firebasemodel1.title);
                                intent.putExtra("content", firebasemodel1.content);
                                intent.putExtra("noteId", docId);

                                startActivity(intent)
                                finish()
                                false
                            }

                        popMenu.menu.add("Delete")
                            .setOnMenuItemClickListener{

                                var documentReference =
                                    mStore!!.collection("notes").document(mUser!!.uid)
                                        .collection("myNotes").document(docId)

                                documentReference.delete().addOnSuccessListener {
                                    Toast.makeText(baseContext,
                                        "this is delete",
                                        Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(baseContext,
                                        "fail to delete",
                                        Toast.LENGTH_SHORT).show()

                                }


                                false
                            }
                        popMenu.show()
                    }

                }


            }

        recycleNote.adapter = noteAdapter


    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var notetitle: TextView = itemView.tvnotetitle
        var notecontent: TextView = itemView.tvNoteContent
        var mnote: LinearLayout = itemView.lineNote
    }


    override fun onStart() {
        super.onStart()
        noteAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (noteAdapter != null) {
            noteAdapter!!.stopListening()
        }

    }

    private fun getRandomColor(): Int {
        var colorCode = ArrayList<Int>()
        colorCode.add(R.color.color1)
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.lightgreen);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.color1)
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.green);

        var random = Random
        var number = random.nextInt(colorCode.size)
        return colorCode[number]

    }
}