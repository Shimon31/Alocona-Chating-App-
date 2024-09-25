package com.iaa2401.aloconachattingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.iaa2401.aloconachattingapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)



        binding.logoutBtn.setOnClickListener {

            val auth = FirebaseAuth.getInstance()

            auth.signOut().apply {

                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

            }



        }


        return binding.root


    }
}