package com.app.usersapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.app.usersapp.data.User
import com.app.usersapp.data.UsersDatabase
import com.app.usersapp.data.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UsersRepository(UsersDatabase.getDatabase(application).userDao())

    val allUsers = MutableLiveData<List<User>>()

    init {
        viewModelScope.launch {
            if (userRepository.getUsers().isEmpty()) { // add few users for illustration
                // the id is auto generated ( @PrimaryKey(autoGenerate = true))
                userRepository.insert(User(0, "https://rickandmortyapi.com/api/character/avatar/240.jpeg", "Dummy", "03-2999777", "a.g@gmail.com"))
                userRepository.insert(User(0, "https://rickandmortyapi.com/api/character/avatar/242.jpeg", "Dummy", "052-2999888", "b@h.com"))
                userRepository.insert(User(0, "https://rickandmortyapi.com/api/character/avatar/241.jpeg", "Dummy", "055-3999883", "boz@h.com"))
                userRepository.insert(User(0, "https://rickandmortyapi.com/api/character/avatar/242.jpeg", "Dummy", "02-8888889", "x@y.com"))
            }
            allUsers.value = userRepository.getUsers()
        }
    }

    fun addUserToDatabase(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.insert(user)
        allUsers.postValue(userRepository.getUsers())
    }

    fun updateUserInDatabase(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.update(user)
        allUsers.postValue(userRepository.getUsers())
    }

    fun deleteUsersFromDataBase(users: List<User>) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.delete(users)
        allUsers.postValue(userRepository.getUsers())
    }
}

class UserViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UserViewModel(application) as T
    }
}