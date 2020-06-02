package com.example.d4app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    /** Called when the user taps the D4 Logo button */
    fun sendMessage(view: View) {
        val intent = Intent(this, DisplayMessageActivity::class.java)
        startActivity(intent)
    }

}
