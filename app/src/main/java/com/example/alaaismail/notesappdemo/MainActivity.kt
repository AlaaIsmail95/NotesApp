package com.example.alaaismail.notesappdemo

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var ListOfNotes = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var myNotesAdapter = MyNotesAdapter(this,ListOfNotes)
        listView_notes.adapter = myNotesAdapter

        //Load data from Database

        LoadQuery("%")


    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    fun LoadQuery(title: String) {
        var dbManger = dbManger(this)
        val selectionArgs = arrayOf(title)
        val projection = arrayOf("ID","Title","Description")
        val cursor = dbManger.Query(projection, "Title like ?", selectionArgs, "Title")

        ListOfNotes.clear()

        if (cursor.moveToFirst()) {
            do {
                var ID = cursor.getInt(cursor.getColumnIndex("ID"))
                var title = cursor.getString(cursor.getColumnIndex("Title"))
                var description = cursor.getString(cursor.getColumnIndex("Description"))

                ListOfNotes.add(Note(ID, title, description))

            } while (cursor.moveToNext())


        }
        var myNotesAdapter = MyNotesAdapter(this,ListOfNotes)
        listView_notes.adapter = myNotesAdapter
    }

    inner class MyNotesAdapter : BaseAdapter {

        var ListOfNotesAdapter = ArrayList<Note>()

        var context:Context?=null
        constructor(context: Context,ListOfNotesAdapter: ArrayList<Note>) : super() {
            this.ListOfNotesAdapter = ListOfNotesAdapter
            this.context=context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myNote = ListOfNotesAdapter[p0]

            myView.Tvtitle.text = myNote.noteName
            myView.TVcontent.text = myNote.noteDesc

            myView.bu_delete.setOnClickListener {
                var dbManger = dbManger(this.context!!)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManger.Delete("ID= ?",selectionArgs)
                LoadQuery("%")

            }
            myView.bu_edite.setOnClickListener{
               GoToUpdate(myNote)
            }
            return myView

        }

        override fun getItem(p0: Int): Any {
            return ListOfNotesAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return ListOfNotesAdapter.size
        }

    }

    fun GoToUpdate(note:Note){
        var intent = Intent(this, add_note::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("Name",note.noteName)
        intent.putExtra("Desc",note.noteDesc)

        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Toast.makeText(applicationContext, p0, Toast.LENGTH_LONG).show()
                LoadQuery("%"+p0+"%")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.add_note_menu -> {
                    var intent = Intent(this, add_note::class.java)
                    startActivity(intent)

                }


            }
        }
        return super.onOptionsItemSelected(item)
    }
}
