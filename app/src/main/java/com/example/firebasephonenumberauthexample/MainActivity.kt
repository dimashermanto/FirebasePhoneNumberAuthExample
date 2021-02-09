package com.example.firebasephonenumberauthexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val signOutButton = findViewById<Button>(R.id.sign_out_button)
        signOutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, PhoneAuthenticationActivity::class.java))

            Toast.makeText(this, "Signed out !", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onStart() {
        super.onStart()

        if(mAuth.currentUser == null){
            startActivity(Intent(this, PhoneAuthenticationActivity::class.java))
        }else{
            Toast.makeText(this, "Already signed in", Toast.LENGTH_SHORT).show()
        }

    }


}