package com.app.usersapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.app.usersapp.R
import com.app.usersapp.data.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.user_row_layout.view.*

class UserListRecyclerViewAdapter : RecyclerView.Adapter<UserListRecyclerViewAdapter.UserViewHolder>() {

    class UserViewHolder(val userView: View) : RecyclerView.ViewHolder(userView)

    var users = emptyList<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var userRowClicked: (User) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userRowView =  LayoutInflater.from(parent.context).inflate(R.layout.user_row_layout, parent, false)
        return UserViewHolder(userRowView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val imageView = holder.userView.userImageView
        Glide.with(imageView)
            .load(users[position].userImage)
            .error(R.drawable.n_a)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)

        holder.userView.userNameTextView.text = users[position].userFullName
        holder.userView.markedCheckBox.isChecked = users[position].marked
        setUserRowClickListener(holder.userView, position)
        setMarkedCheckBoxChangeListener(holder.userView.markedCheckBox, position)
    }

    override fun getItemCount() = users.size

    private fun setUserRowClickListener(userRowView: View, position: Int) = userRowView.setOnClickListener {
        userRowClicked(users[position])
    }

    private fun setMarkedCheckBoxChangeListener(markedCheckBox: CheckBox, position: Int) = markedCheckBox.setOnCheckedChangeListener { _, isChecked ->
        users[position].marked = isChecked
    }
}