package com.example.videocall

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.videocall.databinding.ActivityPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    lateinit var storedVerificationId :String

    private lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var binding: ActivityPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")

        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("fr")

        val getotp = binding.btn

        getotp.setOnClickListener {
            login()
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(credential,email.toString(),name.toString())
            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()

                } else if (e is FirebaseTooManyRequestsException) {

                }
            }


            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId

                verifytheotp(verificationId,email.toString(),name.toString())
            }
        }

    }

    private fun verifytheotp(storedVerificationId: String,email:String,name:String) {
        val verify = binding.otpText
        val vbtn = binding.o2log
        vbtn.setOnClickListener {
            var otp = verify.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId, otp)
                signInWithPhoneAuthCredential(credential,email,name)
            }else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,email:String,name:String) {
        val num = binding.phn.text.toString()

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this,FinalActivity::class.java)
                    intent.putExtra("number",num)
                    intent.putExtra("email",email)
                    intent.putExtra("name",name)
                    startActivity(intent)

                    Toast.makeText(this,"successful",Toast.LENGTH_SHORT).show()

                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun login() {
        var num = binding.phn.text.toString().trim()
        if(!num.isEmpty()){
            num = "+91"+num
            sendVerificationcode(num)
        }else{
            Toast.makeText(this,"Enter your number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationcode(num: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(num)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

}


