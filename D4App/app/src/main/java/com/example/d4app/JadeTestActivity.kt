package com.example.d4app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        val textView = findViewById<TextView>(R.id.text)

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
            override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })


    }

}



