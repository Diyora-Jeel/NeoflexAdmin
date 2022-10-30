package com.nextininfotech.neoflexadmin.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.nextininfotech.neoflexadmin.R
import kotlinx.android.synthetic.main.activity_add_data.*

class AddDataActivity : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var hindiMovieRef: DatabaseReference = database.getReference("hindiMovie")
    private var englishMovieRef: DatabaseReference = database.getReference("englishMovie")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        HMSaveBtn.setOnClickListener {

            if (HMTitle.text.toString().isNotEmpty() && HMPhoto.text.toString()
                    .isNotEmpty() && HMLink.text.toString().isNotEmpty()
            ) {

                val tsLong = System.currentTimeMillis() / 1000
                val timestamp = tsLong.toString()

                val item: HashMap<String, String> = HashMap()

                item["name"] = HMTitle.text.toString()
                item["photo"] = HMPhoto.text.toString()
                item["link"] = HMLink.text.toString()

                hindiMovieRef.child(timestamp).setValue(item)

                HMTitle.text.clear()
                HMPhoto.text.clear()
                HMLink.text.clear()

                Toast.makeText(this, "Insert Data Successful", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show()
            }
        }

        EMSaveBtn.setOnClickListener {

            if (EMTitle.text.toString().isNotEmpty() && EMPhoto.text.toString()
                    .isNotEmpty() && EMLink.text.toString().isNotEmpty()
            ) {

                val tsLong = System.currentTimeMillis() / 1000
                val timestamp = tsLong.toString()

                val item: HashMap<String, String> = HashMap()

                item["name"] = EMTitle.text.toString()
                item["photo"] = EMPhoto.text.toString()
                item["link"] = EMLink.text.toString()

                englishMovieRef.child(timestamp).setValue(item)

                EMTitle.text.clear()
                EMPhoto.text.clear()
                EMLink.text.clear()

                Toast.makeText(this, "Insert Data Successful", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show()
            }
        }
    }
}