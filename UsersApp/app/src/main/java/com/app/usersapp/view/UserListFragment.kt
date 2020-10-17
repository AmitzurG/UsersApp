package com.app.usersapp.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.usersapp.R
import kotlinx.android.synthetic.main.fragment_user_list.*

class UserListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_user_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.user_list_fragment_label)
        setAddUserFabButton()
        setUsersRecyclerView()
        observeUserListChanges()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                deleteMarkedUsers()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAddUserFabButton() = addUserFab.setOnClickListener {
        val action = UserListFragmentDirections.actionListToUserDetails()
        findNavController().navigate(action)
    }

    private fun setUsersRecyclerView() = usersRecyclerView.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        addItemDecoration(dividerItemDecoration)
        adapter = UserListRecyclerViewAdapter()
        (adapter as UserListRecyclerViewAdapter).userRowClicked = { user ->
            val action = UserListFragmentDirections.actionListToUserDetails(
                user.id, user.userImage, user.userFullName, user.userPhone, user.userEmail, newUser = false
            )
            findNavController().navigate(action)
        }
    }

    private fun observeUserListChanges() = (activity as UserActivity).userViewModel.allUsers.observe(viewLifecycleOwner, {
        (usersRecyclerView.adapter as UserListRecyclerViewAdapter).users = it
    })

    private fun deleteMarkedUsers() {
        val viewModel = (activity as UserActivity).userViewModel
        val usersToDelete = viewModel.allUsers.value?.filter { it.marked  }
        if (usersToDelete != null) {
            viewModel.deleteUsersFromDataBase(usersToDelete)
        }
    }
}