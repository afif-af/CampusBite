package com.example.admincampusbite.model

import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody

object CloudinaryUploader {
    private const val CLOUD_NAME = "tuuasil"
    private const val UPLOAD_PRESET = "campusbite_upload"

    fun uploadImage(imageFile: File, onResult: (String?) -> Unit) {
        val client = OkHttpClient()

        val mediaType = "image/*".toMediaType() // ✅ use extension instead of deprecated parse()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", imageFile.name, imageFile.asRequestBody(mediaType)) // ✅ updated
            .addFormDataPart("upload_preset", UPLOAD_PRESET)
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string() // ✅ 'body' is now a property
                    val json = JSONObject(responseData ?: "")
                    val imageUrl = json.optString("secure_url", null)
                    onResult(imageUrl) // ✅ Return only the image URL
                } else {
                    onResult(null)
                }
            }
        })
    }
}
