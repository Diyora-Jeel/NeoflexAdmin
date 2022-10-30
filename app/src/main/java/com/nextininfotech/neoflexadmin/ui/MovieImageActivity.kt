package com.nextininfotech.neoflexadmin.ui

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.nextininfotech.neoflexadmin.R
import com.nextininfotech.neoflexadmin.adapter.ImageAdapter
import com.nextininfotech.neoflexadmin.api.MovieService
import com.nextininfotech.neoflexadmin.constants.AppConstants
import com.nextininfotech.neoflexadmin.constants.CommonUtils
import com.nextininfotech.neoflexadmin.model.image.Image
import com.nextininfotech.neoflexadmin.onClick.MainOnclick
import kotlinx.android.synthetic.main.activity_movie_image.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class MovieImageActivity : AppCompatActivity(), MainOnclick {

    var movieId = 0

    private lateinit var imageAdapter: ImageAdapter

    private lateinit var commonUtils: CommonUtils
    private lateinit var dialog: Dialog

    private lateinit var movieService: MovieService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_image)

        init()

        movieId = intent.getIntExtra("movieId", 0)

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieService = retrofit.create(MovieService::class.java)

        val imagesCall = movieService.getImage(movieId, AppConstants.API_KEY)

        imagesCall.enqueue(object : Callback<Image>
        {
            override fun onResponse(call: Call<Image>, response: Response<Image>) {

                val data = response.body()!!
                if (data.backdrops.isNotEmpty()) {
                    imagesRecyclerView.layoutManager = LinearLayoutManager(this@MovieImageActivity)
                    imageAdapter = ImageAdapter(data.backdrops, this@MovieImageActivity,this@MovieImageActivity)
                    imagesRecyclerView.adapter = imageAdapter
                    imagesNoDataTv.visibility = View.GONE
                }
                else
                {
                    imagesNoDataTv.visibility = View.VISIBLE
                }
                commonUtils.dismissCustomDialog(dialog)

            }

            override fun onFailure(call: Call<Image>, t: Throwable) {

            }
        })
    }

    private fun init() {

        commonUtils = CommonUtils()
        dialog = commonUtils.createCustomLoader(this, false)
        commonUtils.showCustomDialog(dialog,this)
    }

    override fun onClick(movieId: Int) {

    }

    override fun onClick2(movieId: Int) {

    }

    override fun onClick3(image: String) {

        commonUtils.showCustomDialog(dialog,this)

        Glide.with(this)
            .load(image)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                     resource: Drawable,
                     transition: Transition<in Drawable?>?
                ) {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    saveMediaToStorage(bitmap)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }


    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Image saved to Gallery" , Toast.LENGTH_SHORT).show()
            commonUtils.dismissCustomDialog(dialog)
        }
    }

}
