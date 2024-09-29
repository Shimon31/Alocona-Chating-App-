package com.iaa2401.aloconachattingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iaa2401.aloconachattingapp.databinding.ProfileItemBinding

class UserAdapter(var itemClick: ItemClick) : ListAdapter<User, UserViewHolder>(comparator) {

    interface ItemClick{

        fun onItemClick(user:User)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ProfileItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        getItem(position).let {

            holder.binding.apply {

                pfName.text = it.fullName
                gmailName.text = it.email
                bioTV.text = it.bio
            }

            holder.itemView.setOnClickListener { _ ->

                itemClick.onItemClick(it)

            }


        }


    }

    companion object{

        var comparator = object : DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem== newItem
            }


        }

    }


}


class UserViewHolder(var binding: ProfileItemBinding) : RecyclerView.ViewHolder(binding.root) {

}