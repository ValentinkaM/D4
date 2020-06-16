package com.example.d4app

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_form_fields.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class FormFields : AppCompatActivity() {

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

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


        // Camera functionality
        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{

                openCamera()
            }
        }


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


    // camera functionality

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            image_view.setImageURI(image_uri)
        }
    }


}