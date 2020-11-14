package com.example.imagepusher

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CAMERA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        take_photo_button.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        }

        upload_button.setOnClickListener {
            uploadImage(
                image = image_view.drawable.toBitmap(),
                title = title_view.text.toString(),
                description = description_view.text.toString()
            )
        }
    }

    private fun uploadImage(image: Bitmap, title: String, description: String) {
        GlobalScope.launch {
            NetworkService().getService()
                .postImage(
                    "Client-ID 0734710f49bbb68",
                    title,
                    description,
                    getBase64Image(image)
                ).enqueue(object : retrofit2.Callback<ResponseArray> {
                    override fun onResponse(
                        call: Call<ResponseArray>,
                        response: Response<ResponseArray>
                    ) {
                        Toast.makeText(
                            applicationContext,
                            "Photo is uploaded: ${response.body()?.success}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: Call<ResponseArray>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Error has occurred: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(application.packageName, t.message)
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            image_view.setImageBitmap(bitmap)
        }
    }

    fun getBase64Image(image: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val b = outputStream.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}