package com.nextininfotech.neoflexadmin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.nextininfotech.neoflexadmin.R
import kotlinx.android.synthetic.main.activity_app.*

class AppActivity : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var appRef: DatabaseReference = database.getReference("app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        appRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val showMovie = snapshot.child("showMovie").value.toString().toBoolean()
                val showAd = snapshot.child("showAd").value.toString().toBoolean()

                if(showMovie) {
                    movieShowBtn.text = "Show Movie"
                    movieShowBtn.setBackgroundResource(R.drawable.fill_shape_red)
                    movieShowBtn.setTextColor(ContextCompat.getColor(this@AppActivity, R.color.white))
                }
                else {
                    movieShowBtn.text = "Hide Movie"
                    movieShowBtn.setBackgroundResource(R.drawable.fill_shape_white)
                    movieShowBtn.setTextColor(ContextCompat.getColor(this@AppActivity, R.color.black))
                }

                if(showAd) {
                    AdShowBtn.text = "Show Admob ad"
                    AdShowBtn.setBackgroundResource(R.drawable.fill_shape_red)
                    AdShowBtn.setTextColor(ContextCompat.getColor(this@AppActivity, R.color.white))
                }
                else {
                    AdShowBtn.text = "Hide Admob ad"
                    AdShowBtn.setBackgroundResource(R.drawable.fill_shape_white)
                    AdShowBtn.setTextColor(ContextCompat.getColor(this@AppActivity, R.color.black))
                }

                movieShowBtn.setOnClickListener {
                    if(showMovie)
                    {
                        appRef.child("showMovie").setValue(false)
                    }
                    else
                    {
                        appRef.child("showMovie").setValue(true)
                    }
                }

                AdShowBtn.setOnClickListener {
                    if(showAd)
                    {
                        appRef.child("showAd").setValue(false)
                    }
                    else
                    {
                        appRef.child("showAd").setValue(true)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}