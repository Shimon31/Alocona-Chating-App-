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
import com.iaa2401.aloconachattingapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginBtn.setOnClickListener {

            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()

            if (isEmailValid(email) && isPasswordValid(password)) {
                loginUser(email, password)

            }else{

                Toast.makeText(requireContext(), "Invalid email and Password", Toast.LENGTH_SHORT).show()

            }

        }




        return binding.root
    }

    private fun loginUser(email: String, password: String) {

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(
                    requireContext(),
                    "Login Successfully ${user?.email} ",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


    fun isEmailValid(email: String): Boolean {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    fun isPasswordValid(password: String): Boolean {

//        val passRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
//        return password.matches(passRegex)

        return password.length >=6
    }

}

