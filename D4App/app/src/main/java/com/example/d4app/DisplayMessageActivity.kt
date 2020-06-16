package com.example.d4app

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_display_message.*
import java.util.*
import java.text.SimpleDateFormat

class DisplayMessageActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener{
    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    val cal : Calendar = Calendar.getInstance()
   // var textview_date:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        pickDate()

        val EditText5 = findViewById<EditText>(R.id.editText5)
        val EditText4 = findViewById<EditText>(R.id.editText4)
        val EditText2 = findViewById<EditText>(R.id.editText2)
        val EditText3 = findViewById<EditText>(R.id.editText3)
        val button = findViewById<Button>(R.id.button)

        //handle button click
        button.setOnClickListener{
            //get text from edit text
            var name = EditText2.text.toString()
            var age = EditText3.text.toString()
            var height = EditText4.text.toString()
            var weight = EditText5.text.toString()

            //intent to start activit
            val intent = Intent (this@DisplayMessageActivity, SecondSavedInfoActivity::class.java)
            intent.putExtra("Name", name)
            intent.putExtra("Age", age)
            intent.putExtra("Height", height)
            intent.putExtra("Weight", weight)
            startActivity(intent)
        }

    }
    /** Called when the user taps the Save button */
    fun sendMessage(view: View) {
        // Do something in response to button
    }

    private fun getDateCalendar() {
        //val cal : Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }
    private fun pickDate() {
        pickDateBtn.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(this, this, year, month, day).show()
            //textview_date!!.text = "--/--/----"
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month +1
        savedYear = year

        getDateCalendar()
        //updateDateInView()

        DateTv.text = "$savedMonth-$savedDay-$savedYear"
    }
    /*private fun updateDateInView(){
        val myFormat = "MM/dd/yyyy" //the format we need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())

    }*/
}