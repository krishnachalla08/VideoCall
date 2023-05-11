package com.example.videocall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.videocall.databinding.ActivityMainBinding
import com.example.videocall.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth:FirebaseAuth
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()

        binding.btn.setOnClickListener {
            val name = binding.editText1.text.toString()
            val email = binding.editText2.text.toString().trim()
            val pass = binding.editText3.text.toString().trim()
            if (pass.isNotEmpty() && email.isNotEmpty() && name.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            val profiles = hashMapOf<String,Any>(
                                "name" to name,
                                "email" to email,
                                "password" to pass
                            )
                            fd.collection("profiles").document("$email")
                                .set(profiles)
                                .addOnSuccessListener {
                                    Toast.makeText(this,"successfully saved",Toast.LENGTH_SHORT).show()
                                    binding.editText1.text.clear()
                                    binding.editText2.text.clear()


                                }.addOnFailureListener {
                                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                                }
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
            }else{
                Toast.makeText(this,"enter the details!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.r2l.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}