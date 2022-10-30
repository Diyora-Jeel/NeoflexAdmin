package com.nextininfotech.neoflexadmin.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.nextininfotech.neoflexadmin.R
import com.nextininfotech.neoflexadmin.adapter.WatchMovieAdapter
import com.nextininfotech.neoflexadmin.constants.CommonUtils
import com.nextininfotech.neoflexadmin.model.MovieWatchModel
import com.nextininfotech.neoflexadmin.onClick.ImageVideoOnClick
import kotlinx.android.synthetic.main.activity_delete_hindi_mactivity.*


class DeleteHindiMActivity : AppCompatActivity(), ImageVideoOnClick {

    private lateinit var commonUtils: CommonUtils
    private lateinit var dialog: Dialog

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var hindiMovieRefer: DatabaseReference = database.getReference("hindiMovie")

    private lateinit var movieWatchModel: MovieWatchModel
    var dataList = ArrayList<MovieWatchModel>()

    lateinit var watchMovieAdapter: WatchMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_hindi_mactivity)

        init()

        hindiMovieRefer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                    for (item in snapshot.children) {
                        movieWatchModel = MovieWatchModel()
                        movieWatchModel.name = item.child("name").value.toString()
                        movieWatchModel.photo = item.child("photo").value.toString()
                        movieWatchModel.link = item.child("link").value.toString()
                        movieWatchModel.postKey = item.key
                        dataList.add(movieWatchModel)
                    }

                if (dataList.isNotEmpty()) {

                    dataList.reverse()

                    hindiRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                    watchMovieAdapter = WatchMovieAdapter(dataList, this@DeleteHindiMActivity, this@DeleteHindiMActivity)
                    hindiRecyclerView.adapter = watchMovieAdapter
                } else {
                    hindiNoDataTv.visibility = View.VISIBLE
                }
                commonUtils.dismissCustomDialog(dialog)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        hindiSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return false
            }
        })

    }

    private fun filter(newText: String?) {

        val filteredlist: ArrayList<MovieWatchModel> = ArrayList()

        for (item in dataList) {
            if (item.name.toString().lowercase().contains(newText.toString().lowercase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            watchMovieAdapter.filterList(filteredlist)
        }
    }

    private fun init() {

        commonUtils = CommonUtils()
        dialog = commonUtils.createCustomLoader(this, false)
        commonUtils.showCustomDialog(dialog, this)
    }

    override fun onClickIV(postKey: String, position: Int) {
        hindiMovieRefer.child(postKey).removeValue()
        Toast.makeText(this, "Delete Successfully", Toast.LENGTH_SHORT).show()
        dataList.clear()
    }
}