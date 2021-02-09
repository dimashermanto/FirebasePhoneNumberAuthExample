package com.example.firebasephonenumberauthexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthenticationActivity : AppCompatActivity() {

    lateinit var veriBtn: Button
    lateinit var authBtn: Button
    lateinit var phnNoTxt: EditText
    lateinit var verifiTxt: EditText
    lateinit var progress: ProgressBar


    lateinit var mCallbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mAuth: FirebaseAuth
    var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_authentication)

        veriBtn = findViewById(R.id.veriBtn)
        authBtn = findViewById(R.id.authBtn)
        phnNoTxt = findViewById(R.id.phnNoTxt)
        progress = findViewById(R.id.progress)
        verifiTxt = findViewById(R.id.verifiTxt)

        mAuth = FirebaseAuth.getInstance()

        veriBtn.setOnClickListener {
            progress.visibility = View.VISIBLE
            verify()
        }

        authBtn.setOnClickListener {
            progress.visibility = View.VISIBLE
            authenticate()
        }
    }

    private fun verificationCallback(){
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                progress.visibility = View.GONE
                Log.i("VerificationFail", "${p0.smsCode} (Completed !)")
                signIn(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.i("VerificationFail", "${p0.message} ")
                Toast.makeText(this@PhoneAuthenticationActivity, "${p0.message}", Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }

            override fun onCodeSent(verification: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verification, p1)

                Log.i("VerificationFail", "${verification} ")
                verificationId = verification

                Toast.makeText(this@PhoneAuthenticationActivity, "Code sent to your number !", Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }

        }
    }

    private fun signIn(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this@PhoneAuthenticationActivity, "Login succesfully !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }


    private fun authenticate() {
        val verificationNo = verifiTxt.text.toString().trim()
        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, verificationNo)

        signIn(credential)
    }


    private fun verify() {
        val phoneNumber = phnNoTxt.text.toString().trim()

        verificationCallback()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks)
    }

}