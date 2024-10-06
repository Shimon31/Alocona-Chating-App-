package com.iaa2401.aloconachattingapp

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.iaa2401.aloconachattingapp.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding

    lateinit var userDB : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        userDB = FirebaseDatabase.getInstance().reference

        binding.signInBtn.setOnClickListener {


            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            val user = binding.userName.text.toString().trim()

            if (isEmailValid(email) && isPasswordValid(password)) {

                signInUser(email, password, user)
            }else{

                Toast.makeText(requireContext(), "Invalid email and Password", Toast.LENGTH_SHORT).show()
            }


        }


        return binding.root
    }

    private fun signInUser(email: String, password: String, user: String) {

        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                saveUserToDatabase(auth.currentUser?.uid,email,user)


            } else {

                Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT)
                    .show()

            }


        }


    }

    private fun saveUserToDatabase(uid: String?, email: String, user: String) {

        uid?.let {

            val user = User(userId = uid,email= email,fullName = user)

            userDB.child(DBNODES.USER).child(it).setValue(user).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)

                } else {

                    Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()

                }

            }


        }


    }

    fun isEmailValid(email: String): Boolean {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    fun isPasswordValid(password: String): Boolean {

        val passRegex = Regex("^(?=.*[A-Za-z])(?=.*[@\$!%*#?&])[A-Za-z@\$!%*#?&\\d]{6,}\$")
        return password.matches(passRegex)

//        return password.length >=6
    }


}