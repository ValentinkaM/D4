package com.example.d4app

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class FormFields : AppCompatActivity() {

    var picker: DatePickerDialog? = null
    var eText: EditText? = null
    var btnGet: Button? = null
    var tvw: TextView? = null

    var userAge: Int? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_fields)
        val spinner: Spinner = findViewById(R.id.userSex)

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

        tvw = findViewById(R.id.textView1) as TextView
        eText = findViewById(R.id.editText1) as EditText
        eText!!.inputType = InputType.TYPE_NULL
        eText!!.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(
                this@FormFields,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    eText!!.setText(
                        (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                    )
                    userAge = getAge(year, monthOfYear, dayOfMonth)
                    println("age is:" + userAge)
                }, year, month, day
            )


            picker!!.show()
        }
        btnGet = findViewById<View>(R.id.button1) as Button
        btnGet!!.setOnClickListener { tvw!!.text = "Selected Date: " + eText!!.text }



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
