package com.iaa2401.aloconachattingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iaa2401.aloconachattingapp.databinding.FragmentChatBinding
import java.util.UUID

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var chatDB: DatabaseReference
    lateinit var userIdSelf: String
    lateinit var userIdRemote: String


    val chatList = mutableListOf<TextMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        chatDB = FirebaseDatabase.getInstance().reference

        
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true

        binding.messageRCV.layoutManager = layoutManager


        requireArguments().getString(USERID)?.let {
            userIdRemote = it

        }


        FirebaseAuth.getInstance().currentUser?.let {

            userIdSelf = it.uid

        }

        chatDB.child(DBNODES.USER).child(userIdRemote)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(User::class.java)?.let {

                        binding.apply {

                            Glide.with(requireContext()).load(it.profilePic)
                                .placeholder(R.drawable.placeholder).into(profileImage)

                            profileName.text = it.fullName
                            gmailName.text = it.email

                        }


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })



        messageToShow()


        return binding.root
    }

    private fun messageToShow() {
        chatDB.child(DBNODES.CHAT).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                chatList.clear()

                snapshot.children.forEach {its->


                    its.getValue(TextMessage::class.java)?.let {

                        if (it.senderID == userIdSelf && it.receiverID == userIdRemote ||
                            it.senderID == userIdRemote && it.receiverID == userIdSelf){

                            chatList.add(it)
                        }

                    }

                }

                var adapter = ChatAdapter(userIdSelf,chatList)

                binding.messageRCV.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    companion object {

        private var USERID = "id"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendBtn.setOnClickListener {

            var textMessage = TextMessage(
                text = binding.messageET.text.toString(),
                msgID = "",
                senderID = userIdSelf,
                receiverID = userIdRemote
            )
            sendMessage(textMessage)

        }

    }

    private fun sendMessage(textMessage: TextMessage) {

        var msgId = chatDB.push().key ?: UUID.randomUUID().toString()

        textMessage.msgID = msgId


        chatDB.child(DBNODES.CHAT).child(msgId).setValue(textMessage).addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Message Sent Successfully", Toast.LENGTH_SHORT)
                    .show()
                binding.messageET.setText("")
            } else {
                Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }


}