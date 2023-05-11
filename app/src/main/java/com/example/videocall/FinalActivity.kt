package com.example.videocall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.videocall.databinding.ActivityFinalBinding
import com.google.firebase.firestore.FirebaseFirestore

class FinalActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFinalBinding
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val number =intent.getStringExtra("number")
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")

        val mbundle = Bundle().also {
            it.putString("number",number)
            it.putString("name",name)
            it.putString("email",email.toString())
        }


        val fragment=ProfileFragment()
        fragment.arguments=mbundle
        loadFragment(fragment,name.toString(),number.toString(),email.toString())


        binding.navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_profile->loadFragment(ProfileFragment(),name.toString(),number.toString(),email.toString())
                R.id.navigation_call->loadFragment2(CallFragment(),email.toString())
                else->{

                }
            }
            true
        }


    }

    private fun loadFragment2(fragment: Fragment,email:String) {
        fd.collection("profiles").get().addOnCompleteListener {
            for(document in it.result){
                if(document.data.getValue("email").toString()==email){

                    val onname = document.data.getValue("name").toString()
                    val mbund = Bundle()
                    mbund.putString("uname",onname)
                    fragment.arguments=mbund

                }else{
                    val onname = document.data.getValue("name").toString()
                    val mbund = Bundle()
                    mbund.putString("cname",onname)
                    fragment.arguments=mbund
                }
            }
            val transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.host_fragment,fragment)
            transaction.commit()
        }
    }


    private fun loadFragment(fragment: Fragment,name:String,number :String,email: String) {
        val mbundle = Bundle().also {
            it.putString("number",number)
            it.putString("name",name)
            it.putString("email",email)
        }
        fragment.arguments=mbundle

        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.host_fragment,fragment)
        transaction.commit()
    }
}