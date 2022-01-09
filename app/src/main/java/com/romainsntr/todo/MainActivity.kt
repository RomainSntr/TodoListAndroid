package com.romainsntr.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import coil.load
import com.romainsntr.todo.network.Api
import com.romainsntr.todo.tasklist.Task
import com.romainsntr.todo.user.UserInfoActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    var onClickDelete: () -> Unit = {}

    override fun onResume() {
        super.onResume()
        val userInfoTextView = findViewById<TextView>(R.id.userInfoTextView)
        val userAvatarImageView = findViewById<ImageView>(R.id.userAvaterImageView)

        userAvatarImageView.setOnClickListener {
            Log.e("MainActivity", "Button clicked")
            val intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            userInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
             userAvatarImageView.load("https://goo.gl/gEgYUd")
        }
    }


}