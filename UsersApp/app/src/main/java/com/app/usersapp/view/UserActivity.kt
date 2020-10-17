package com.app.usersapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.usersapp.R
import com.app.usersapp.viewmodel.UserViewModel
import com.app.usersapp.viewmodel.UserViewModelFactory

class UserActivity : AppCompatActivity() {

    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        userViewModel = ViewModelProvider(this, UserViewModelFactory(application)).get(UserViewModel::class.java)
    }
}