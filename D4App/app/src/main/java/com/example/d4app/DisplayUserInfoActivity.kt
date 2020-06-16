/**************************************************************************************************
 * AUTHOR: JADE
 * PURPOSE: Functionality for the activity which displays information retrieved via API call.
 *
 * - takes user input (currently hard-coded) and passes it to API call
 * - API call returns BMI information in the form of a JSON object
 * - Parses JSON object and retrieves the relevant values.
 * - Displays values on screen.
 *
 * Tammie, you can probably? add your functionality to this file, or otherwise integrate the
 * functionality however you see fit.
 *
 * Activity should eventually display:
 * - User image
 * - User's name, in a greeting
 * - It doesn't need to /display/ any other input, but will be accessing the other data
 *   for the BMI call
 **************************************************************************************************/

package com.example.d4app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.math.RoundingMode

class DisplayUserInfoActivity : AppCompatActivity() {
    // set up the OkHttpClient to make the call.
    private val client = OkHttpClient()
    // hard coded values standing in for user values
    private var weight = "160"
    private var height = "5-10"
    private var sex = "m"
    private var age = "24"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_user_info)
        setUserData()

        // Call function which makes the call to the API.
        // Eventually, we would want to pass it the user's input values but for now we're just passing it hard-coded values.
        run(weight, height, sex, age)
    }

    fun setUserData()
    {
        val dataValues : String

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
                    //   name.setText(it.substringAfter(":")).toString()
                }
                it.startsWith("birthdate:", true) -> {
                    //birthdate.setText(it.substringAfter(":")).toString()
                }
                it.startsWith("age:", true) -> {
                    //age.setText(it.substringAfter(":")).toString()
                    age = it.substringAfter(":")
                }
                it.startsWith("height:", true) -> {
                    height = it.substringAfter(":")
                }
                it.startsWith("weight:", true) -> {
                    //weight.setText(it.substringAfter(":")).toString()
                    weight = it.substringAfter(":")
                }
                it.startsWith("biorole:", true) -> {
                    sex = setBioRole(it.substringAfter(":"))

                }
            }
        })

    }

    fun setBioRole(roleFromFile: String): String
    {
        return if(roleFromFile.toLowerCase() == "male") {
            "m"
        } else {
            "f"
        }
    }

    fun run(weight: String, height: String, sex: String, age: String) {

        // access the field which displays BMI number.
        val bmiText = findViewById<TextView>(R.id.bmiNum)
        val idealWeightText = findViewById<TextView>(R.id.idealWeight)
        var gson = Gson()
        var myBMI : UserBMIEntity.UserBMIInfo
        var bmiMsg = "nothing here."
        var weightMsg = "nothing here."

        val mediaType = MediaType.parse("application/json")
        val url = "https://bmi.p.rapidapi.com/"
        val body = RequestBody.create(
            mediaType,
            "{\"weight\":{\"value\":\"${weight}\",\"unit\":\"lb\"},\"height\":{\"value\":\"${height}\",\"unit\":\"ft-in\"},\"sex\":\"${sex}\",\"age\":\"${age}\"}"
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("x-rapidapi-host", "bmi.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "c889a80ea5msh0fe36507db194d4p19457ajsn46921218af8f")
            .addHeader("content-type", "application/json")
            .addHeader("accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    myBMI = gson.fromJson(response.body()?.string(), UserBMIEntity.UserBMIInfo::class.java)
                    bmiMsg = "${myBMI.bmi.value}"

                    val str = myBMI.ideal_weight
                    val numbers = Regex("[0-9]+").findAll(str)
                        .map(MatchResult::value)
                        .toList()

                    val weightString = arrayOf("${numbers[0]}.${numbers[1]}", "${numbers[2]}.${numbers[3]}")

                    weightMsg = "${weightString[0].toBigDecimal().setScale(1, RoundingMode.UP).toDouble() * 2.2} lbs to" +
                            "${weightString[1].toBigDecimal().setScale(1, RoundingMode.UP).toDouble() * 2.2} lbs."
                    Handler(Looper.getMainLooper()).post(Runnable {
                        bmiText.apply {
                            text = bmiMsg
                        }
                        idealWeightText.apply {
                            text = weightMsg
                        }
                    })
                }
            }
        })
    }
}

// Class to hold information as retrieved from API call's JSON obect.
class UserBMIEntity {

    data class UserBMIInfo(
        val weight: Weight,
        val height: Height,
        val bmi: BMI,
        val ideal_weight: String,
        val surface_area: String,
        val ponderal_index: String,
        val bmr: BMR,
        val whr: WHR,
        val whtr: WHTR
    )

    data class Weight(
        val kg: String,
        val lb: String
    )
    data class Height(
        val m: String,
        val cm: String,
        val inch: String,
        val ft_in: String
    )
    data class BMI(
        val value: String,
        val status: String,
        val risk: String,
        val prime: String
    )
    data class BMR(
        val value: String
    )
    data class WHR(
        val value: String,
        val status: String
    )
    data class WHTR(
        val value: String,
        val status: String
    )
}

