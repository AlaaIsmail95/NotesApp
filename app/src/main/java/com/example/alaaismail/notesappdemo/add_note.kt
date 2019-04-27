package com.example.alaaismail.notesappdemo

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class add_note : AppCompatActivity() {

    val dbTable = "Notes"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)



        try {
            var bundle: Bundle = intent.extras
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                add_title.setText(bundle.getString("Name").toString())
                note_body.setText(bundle.getString("Desc").toString())
            }
        }catch (e:Exception){

            e.printStackTrace()
        }



        Bu_Add.setOnClickListener {
            var dbManger = dbManger(this)
            var values = ContentValues()
            values.put("Title", add_title.text.toString())
            values.put("Description", note_body.text.toString())

            if (id == 0) {
                val ID = dbManger.Insert(values)
                if (ID > 0) {
                    Toast.makeText(this, "Note has added", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Cannot add note", Toast.LENGTH_SHORT).show()
                }
            } else {
                var selectionArgs = arrayOf(id.toString())
                val ID = dbManger.Update(values,"ID= ?",selectionArgs)
                if (ID > 0) {
                    Toast.makeText(this, "Note has updated", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Cannot update note", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}
