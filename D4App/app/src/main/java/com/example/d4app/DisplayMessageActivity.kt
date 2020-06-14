package com.example.d4app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class DisplayMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        val dataValues : String
        val name = findViewById<EditText>(R.id.displayName)
        val age = findViewById<EditText>(R.id.displayAge)
        val height = findViewById<EditText>(R.id.displayHeight)
        val weight = findViewById<EditText>(R.id.displayWeight)



        val filename = "ProfileData.txt"
            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(filename)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }

        dataValues = stringBuilder.toString()

        var result = dataValues.split(",").map { it.trim() }
        result.forEach(fun(it: String) {
            when {
                it.startsWith("name:", true) -> {
                    name.setText(it.substringAfter(":")).toString()
                }
                it.startsWith("birthdate:", true) -> {
                    // TODO: Implement Age Calculation
                    age.setText(it.substringAfter(":")).toString()
                }
                it.startsWith("heightin:", true) -> {
                    height.setText(it.substringAfter(":")).toString()
                }
                it.startsWith("weight:", true) -> {
                    weight.setText(it.substringAfter(":")).toString()
                }
            }


        })

    }
}