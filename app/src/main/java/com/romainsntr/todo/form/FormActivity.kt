package com.romainsntr.todo.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.romainsntr.todo.R
import com.romainsntr.todo.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        var task = intent.getSerializableExtra("editTask") as? Task

        //var newTask = Task(id = UUID.randomUUID().toString(), title = "defaultTitle")

        if (task != null)
        {
            var editTitle = findViewById<EditText>(R.id.title)
            var editdescription = findViewById<EditText>(R.id.description)

            editTitle.setText(task.title)
            editdescription.setText(task.description)

        }
        else
        {
            task = Task(id = UUID.randomUUID().toString(), title = "defaultTitle")
        }

        val button = findViewById<ImageButton>(R.id.imageButton2)
        button.setOnClickListener {
            val titleText = findViewById<EditText>(R.id.title).text.toString()
            val descriptionText = findViewById<EditText>(R.id.description).text.toString()
            task.title = titleText
            task.description = descriptionText
            intent.putExtra("task", task)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}