package com.nextininfotech.neoflexadmin.ui

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nextininfotech.neoflexadmin.R
import com.nextininfotech.neoflexadmin.adapter.SearchAdapter
import com.nextininfotech.neoflexadmin.api.MovieService
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.API_KEY
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.BASE_URL
import com.nextininfotech.neoflexadmin.constants.CommonUtils
import com.nextininfotech.neoflexadmin.model.movie.Movie
import com.nextininfotech.neoflexadmin.onClick.MainOnclick
import kotlinx.android.synthetic.main.activity_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MovieActivity : AppCompatActivity() , MainOnclick {

    private lateinit var movieService: MovieService
    private lateinit var searchAdapter: SearchAdapter

    private lateinit var commonUtils: CommonUtils
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        init()

        searchBar.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getSearchData(searchBar.text.toString())
                commonUtils.showCustomDialog(dialog, this)
                true
            } else false
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieService = retrofit.create(MovieService::class.java)

        if (commonUtils.isNetworkAvailable(this)) {
            getDataPlaying()
        }
        else {
            commonUtils.showCustomDialog(dialog, this)
            commonUtils.createDialog(
                this,
                R.drawable.ic_no_internet,
                "No Internet",
                "Please check your connection status and try again"
            )
        }

    }

    private fun init() {
        commonUtils = CommonUtils()
        dialog = commonUtils.createCustomLoader(this, false)
        commonUtils.showCustomDialog(dialog, this)
    }

    private fun getDataPlaying() {

        val nowPlayingCall = movieService.getNowPlaying(API_KEY)
        nowPlayingCall.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                val data = response.body()!!

                searchRecyclerView.layoutManager = LinearLayoutManager(this@MovieActivity)
                searchAdapter = SearchAdapter(this@MovieActivity, data.results, this@MovieActivity)
                searchRecyclerView.adapter = searchAdapter
                commonUtils.dismissCustomDialog(dialog)
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {

            }
        })
    }

    private fun getSearchData(query: String) {
        val searchCall = movieService.getSearch(API_KEY, query)

        searchCall.enqueue(object : Callback<Movie> {
            override fun onResponse(
                call: Call<Movie>,
                response: Response<Movie>
            ) {
                val data = response.body()!!
                searchRecyclerView.layoutManager = LinearLayoutManager(this@MovieActivity)
                searchAdapter = SearchAdapter(this@MovieActivity, data.results, this@MovieActivity)
                searchRecyclerView.adapter = searchAdapter
                commonUtils.dismissCustomDialog(dialog)
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
            }
        })
    }

    override fun onClick(movieId : Int) {

        val intent = Intent(this,MovieDetailsActivity::class.java)
        intent.putExtra("movieId",movieId)
        startActivity(intent)
    }

    override fun onClick2(movieId: Int) {
        val intent = Intent(this,MovieImageActivity::class.java)
        intent.putExtra("movieId",movieId)
        startActivity(intent)
    }

    override fun onClick3(image: String) {

    }
}