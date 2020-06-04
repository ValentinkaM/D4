package com.example.d4app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class JadeTestActivity : AppCompatActivity() {
    // set up the OkHttpClient to make the call.
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jade_test)

        // hard coded values standing in for user values
        val weight = "160"
        val height = "5-10"
        val sex = "m"
        val age = "24"

        // Call function which makes the call to the API.
        // Eventually, we would want to pass it the user's input values but for now we're just passing it hard-coded values.
        run(weight, height, sex, age)
    }

    fun run(weight: String, height: String, sex: String, age: String) {

        val textView = findViewById<TextView>(R.id.bmiInfo)
        var gson = Gson()
        var myBMI : UserBMIEntity.UserBMIInfo
        var displayMessage = "nothing here."
        println("initializing displaymessage: ${displayMessage}")

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
                    displayMessage = "Your BMI is ${myBMI.bmi.value}. Your ideal weight based on your information would be ${myBMI.ideal_weight}"
                    println("running after getting response: ${displayMessage}")

                    Handler(Looper.getMainLooper()).post(Runnable {
                        textView.apply {
                            text = displayMessage
                        }
                    })
                }
            }
        })


        println("running at bottom of run function: ${displayMessage}")
    }
}

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



