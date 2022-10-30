package com.nextininfotech.neoflexadmin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nextininfotech.neoflexadmin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addMovieBtn.setOnClickListener {
            val intent = Intent(this,AddDataActivity::class.java)
            startActivity(intent)
        }

        appBtn.setOnClickListener {
            val intent = Intent(this,AppActivity::class.java)
            startActivity(intent)
        }

        movieBtn.setOnClickListener {
            val intent = Intent(this, MovieActivity::class.java)
            startActivity(intent)
        }

        deleteHindiMovieBtn.setOnClickListener {
            val intent = Intent(this, DeleteHindiMActivity::class.java)
            startActivity(intent)
        }

        deleteEnglishMovieBtn.setOnClickListener {
            val intent = Intent(this, DeleteEnglishMActivity::class.java)
            startActivity(intent)
        }

    }
}