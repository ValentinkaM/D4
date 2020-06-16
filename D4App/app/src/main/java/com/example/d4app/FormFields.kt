package com.example.d4app

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class FormFields : AppCompatActivity() {

    var picker: DatePickerDialog? = null
    var birthDay: EditText? = null

    var userAge: Int? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_fields)

        // SETUP ELEMENTS
        val spinner: Spinner = findViewById(R.id.userSex)

        birthDay = findViewById<EditText>(R.id.userBDate)

        val name = findViewById<EditText>(R.id.userName)
        val heightFeet = findViewById<EditText>(R.id.userHeightFt)
        val heightInches = findViewById<EditText>(R.id.userHeightIn)
        val weight = findViewById<EditText>(R.id.userWeight)
        val bioRole = findViewById<Spinner>(R.id.userSex)
        val btnSave = findViewById<Button>(R.id.btnSaveUserProfile)


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.bio_role,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // DATE CALENDAR FUNCTIONALIty
        birthDay!!.inputType = InputType.TYPE_NULL
        birthDay!!.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(
                this@FormFields,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    birthDay!!.setText(
                        (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                    )
                    userAge = getAge(year, monthOfYear, dayOfMonth)
                    println("age is:" + userAge)
                }, year, month, day
            )
            picker!!.show()
        }


        btnSave.setOnClickListener(View.OnClickListener {
        // USED for debugging  Toast.makeText(applicationContext,"data save:" + bioRole.selectedItem.toString(),Toast.LENGTH_LONG).show()


            val userData: String =
                "name:" + name.text.toString() + ",birthdate:" + birthDay.toString() + "age:" + userAge + ",height:" + heightFeet.text.toString() + "-" + heightInches.text.toString() + ",weight:" + weight.text.toString() + ",biorole:" + bioRole.selectedItem.toString()
             writeDataToFile(userData)

           val intent = Intent(this, DisplayUserInfoActivity::class.java)
           startActivity(intent)
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun writeDataToFile(userData: String)
    {

        val file: String = "ProfileData.txt"

        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(userData.toByteArray())
        } catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getAge(year: Int, month: Int, day: Int): Int? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.set(year, month, day)
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.MONTH] <= month && today[Calendar.DAY_OF_MONTH] < day) {
            age--
        }
        val ageInt = age
        return ageInt
    }


}
