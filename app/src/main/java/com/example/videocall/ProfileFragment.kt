package com.example.videocall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.videocall.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private var _binding :FragmentProfileBinding?=null
    private val binding get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        val bund = arguments
        val email = bund!!.getString("email")
        val name = bund!!.getString("name")
        val number = bund!!.getString("number")
        binding.tv12.text=name
        binding.tv22.text=email
        binding.tv32.text=number



        return binding.root
          }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}