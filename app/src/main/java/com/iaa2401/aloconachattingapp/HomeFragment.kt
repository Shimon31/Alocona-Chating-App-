package com.iaa2401.aloconachattingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.bumptech.glide.Glide

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iaa2401.aloconachattingapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), UserAdapter.ItemClick {

    lateinit var binding: FragmentHomeBinding
    lateinit var userDB: DatabaseReference
    lateinit var adapter: UserAdapter


    private var currentUser: User? = null
    private var auth = FirebaseAuth.getInstance()
    lateinit var firebaseUser: FirebaseUser
    private val bundle = Bundle()
    var userList: MutableList<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        userDB = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {
            firebaseUser = it
        }

        binding.profileBtn.setOnClickListener {

            currentUser?.let {

                bundle.putString("id",it.userId)
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment,bundle)

            }


        }


        binding.logoutBtn.setOnClickListener {

            auth.signOut().apply {

                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

            }


        }

        adapter = UserAdapter(this@HomeFragment)
        binding.recyclerView.adapter = adapter

        getAvailableUSer()

        return binding.root

    }

    private fun getAvailableUSer() {

        userDB.child(DBNODES.USER).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                snapshot.children.forEach {

                    val user: User = it.getValue(User::class.java)!!

                    if (firebaseUser.uid != user.userId) {
                        userList.add(user)
                    } else {
                        currentUser = user

                        Glide.with(requireContext()).load(user.profilePic).placeholder(R.drawable.placeholder)
                            .into(binding.profileBtn)

                    }


                }
                adapter.submitList(userList)
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }

//    private fun setProfile() {
//        currentUser?.let {
//
//            binding.profileBtn.load("https://www.google.com/imgres?q=sadek%20bhuiya%20shimon&imgurl=https%3A%2F%2Flookaside.fbsbx.com%2Flookaside%2Fcrawler%2Fmedia%2F%3Fmedia_id%3D492760747028449&imgrefurl=https%3A%2F%2Fm.facebook.com%2F100088835840260&docid=59WDNV_ROTIDAM&tbnid=oIcxOx9tCECHkM&vet=12ahUKEwit-vOChIGJAxW38zgGHeLEAFgQM3oECEkQAA..i&w=1536&h=2048&hcb=2&itg=1&ved=2ahUKEwit-vOChIGJAxW38zgGHeLEAFgQM3oECEkQAA")
//        }
//    }

    override fun onItemClick(user: User) {


        bundle.putString("id", user.userId)
        findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)

    }
}