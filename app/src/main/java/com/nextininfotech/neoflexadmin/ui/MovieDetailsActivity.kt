package com.nextininfotech.neoflexadmin.ui

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.nextininfotech.neoflexadmin.R
import com.nextininfotech.neoflexadmin.api.MovieService
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.API_KEY
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.BASE_URL
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.IMAGE_URL
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.IMAGE_URL_ORIGINAL
import com.nextininfotech.neoflexadmin.constants.CommonUtils
import com.nextininfotech.neoflexadmin.model.moviedetails.MovieDetails
import kotlinx.android.synthetic.main.activity_movie_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MovieDetailsActivity : AppCompatActivity() {

    var movieId = 0
    private lateinit var movieService: MovieService
    private lateinit var commonUtils: CommonUtils
    private lateinit var dialog: Dialog
    var posterUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        init()

        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        movieId = intent.getIntExtra("movieId", 0)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieService = retrofit.create(MovieService::class.java)

        val movieDetailsCall = movieService.getMovieDetails(movieId, API_KEY)

        movieDetailsCall.enqueue(object : Callback<MovieDetails> {
            override fun onResponse(
                call: Call<MovieDetails>,
                response: Response<MovieDetails>
            ) {
                val data = response.body()!!
                Glide.with(this@MovieDetailsActivity).load(IMAGE_URL + data.poster_path).placeholder(R.drawable.ic_no_image).into(imageView)
                posterUrl = data.poster_path
                titleTv.text = data.title
                idTv.text = movieId.toString()
                imageTv.text = data.poster_path
                imageFullTv.text = IMAGE_URL + data.poster_path
                commonUtils.dismissCustomDialog(dialog)
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
            }
        })

        titleBtn.setOnClickListener {
            val clip = ClipData.newPlainText("label",titleTv.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Copy Successfully", Toast.LENGTH_LONG).show()
        }

        idBtn.setOnClickListener {
            val clip = ClipData.newPlainText("label",idTv.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Copy Successfully", Toast.LENGTH_LONG).show()
        }

        imageBtn.setOnClickListener {
            val clip = ClipData.newPlainText("label",imageTv.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Copy Successfully", Toast.LENGTH_LONG).show()
        }

        imageFullBtn.setOnClickListener {
            val clip = ClipData.newPlainText("label",imageFullTv.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Copy Successfully", Toast.LENGTH_LONG).show()
        }


        imageView.setOnClickListener {

            if(posterUrl.isNotEmpty()) {

                commonUtils.showCustomDialog(dialog,this)

                Glide.with(this)
                    .load(IMAGE_URL_ORIGINAL + posterUrl)
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
        }
    }

    private fun init() {

        commonUtils = CommonUtils()
        dialog = commonUtils.createCustomLoader(this, false)
        commonUtils.showCustomDialog(dialog,this)
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