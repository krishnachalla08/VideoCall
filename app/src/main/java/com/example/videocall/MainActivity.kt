package com.example.videocall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.videocall.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()
        binding.btn.setOnClickListener {
            val email = binding.editText1.text.toString().trim()
            val pass = binding.editText2.text.toString().trim()
            if (pass.isNotEmpty() && email.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            fd.collection("profiles")
                                .get()
                                .addOnCompleteListener {
                                    for(document in it.result!!){
                                        if(document.data.getValue("email")==email){
                                            val name = document.data.getValue("name").toString()

                                            Toast.makeText(this,"Login successful",Toast.LENGTH_SHORT).show()

                                            val intent = Intent(this,PhoneActivity::class.java)
                                            intent.putExtra("email",email)
                                            intent.putExtra("name",name)
                                            startActivity(intent)
                                        }
                                    }
                                }


                        }
                    }.addOnFailureListener {
                        Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this,"Enter credentials!!",Toast.LENGTH_SHORT).show()
            }
        }
        binding.l2r.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}


