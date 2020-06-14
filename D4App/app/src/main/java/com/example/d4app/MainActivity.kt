package com.example.d4app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = findViewById<EditText>(R.id.editPersonName)
        val birthDate = findViewById<EditText>(R.id.editBirthdate)
        val heightFeet = findViewById<EditText>(R.id.editHeightInFeet)
        val heightInches = findViewById<EditText>(R.id.editHeightIn)
        val weight = findViewById<EditText>(R.id.editWeight)

        val btnSave = findViewById<Button>(R.id.buttonSave)
       //  val btnView = findViewById<Button>(R.id.btnView)


        btnSave.setOnClickListener(View.OnClickListener {
            val file:String = "ProfileData.txt"
            val data:String = "name:" + name.text.toString() + ",birthdate:" + birthDate.text.toString() + ",heightFeet:" + heightFeet.text.toString() + ",heightInches:" + heightInches.text.toString() + ",weight:" + weight.text.toString()
                /*
            val name:String = "name:" + name.text.toString()
            val birthDate:String = "birthdate:" + birthDate.text.toString()
            val heightFeet:String = "heightFeet:" + heightFeet.text.toString()
            val heightInches:String = "heightInches:" + heightInches.text.toString()
            val weight:String = "weight:" + weight.text.toString()
            */

            val fileOutputStream: FileOutputStream
            try {
                fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
                fileOutputStream.write(data.toByteArray())
            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }catch (e: NumberFormatException){
                e.printStackTrace()
            }catch (e: IOException){
                e.printStackTrace()
            }catch (e: Exception){
                e.printStackTrace()
            }
            Toast.makeText(applicationContext,"data save",Toast.LENGTH_LONG).show()
           // fileName.text.clear()
           // fileData.text.clear()
            val intent = Intent(this, DisplayMessageActivity::class.java)
            startActivity(intent)
        })


    }
    /** Called when the user taps the D4 Logo button */
    fun sendMessage(view: View) {
        val intent = Intent(this, DisplayMessageActivity::class.java)
        startActivity(intent)
    }

}

