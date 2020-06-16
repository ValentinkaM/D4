package com.example.d4app

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView

class SecondSavedInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_saved_info)

        //get data from intent
        var intent = intent

        val name = intent.getStringExtra("Name")
        val age = intent.getStringExtra("Age")
        val height = intent.getStringExtra("Height")
        val weight = intent.getStringExtra("Weight")

        //text view
        val resultTv = findViewById<TextView>(R.id.resultTv)
        //set Text
            resultTv.text =
                "Name: "+name+"\nAge: "+age+ "\nHeight: "+height+ "\nWeight: "+ weight






    }
}
