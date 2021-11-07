package com.example.ecocook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MessageActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val message = intent.getSerializableExtra("message") as Message

    }
}