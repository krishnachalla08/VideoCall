package com.example.videocall


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.videocall.databinding.FragmentCallBinding


class CallFragment : Fragment() {
    private var _binding : FragmentCallBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCallBinding.inflate(inflater,container,false)
        val bund = arguments
        val username = bund?.getString("uname").toString()
        var callname = bund?.getString("cname").toString()
        binding.tv1.text=callname

        binding.callbtn.setOnClickListener {
            val userId = username
            if (userId.isEmpty()) {
                return@setOnClickListener
            }

            val intent = Intent(activity,FinalActivity2::class.java)
            startActivity(intent)

        }

        return binding.root
    }





    override fun onDestroyView() {
        super.onDestroyView()
        unInit()
    }
}