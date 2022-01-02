package com.romainsntr.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.romainsntr.todo.network.Api
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val userInfoTextView = findViewById<TextView>(R.id.userInfoTextView)

        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            userInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
        }
    }


}