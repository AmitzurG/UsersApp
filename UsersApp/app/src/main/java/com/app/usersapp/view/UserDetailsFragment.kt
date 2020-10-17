package com.app.usersapp.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.usersapp.R
import com.app.usersapp.data.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_user_details.*
import java.util.regex.Pattern
import kotlin.random.Random

class UserDetailsFragment : Fragment() {

    private val args by navArgs<UserDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_user_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.user_details_fragment_label)
        setUserImageOnClickListener()
        setViewMode(!args.newUser) // if this is an exist user, open the form in view mode with the user details (the fields aren't editable)
        if (!args.newUser) {
            setUserDetails()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_details, menu)
        if (args.newUser) { // if this is a new user, the details form is opened in edit mode, and the action need be "Save" (the fields are editable)
            val menuItem = menu.findItem(R.id.action_edit_or_save)
            menuItem.title = getString(R.string.action_save)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_or_save -> {
                if (item.title == getString(R.string.action_edit)) { // edit user
                    // move the details form to edit mode, possible to edit the fields
                    item.title = getString(R.string.action_save)
                    setViewMode(false)
                } else if (areFieldsValid()) { // save user
                    val user = User(
                        args.userId, // for a new user the id is automatically generated (autoGenerate = true), for exist user her id is used
                        (userImageView?.tag as? String) ?: "",
                        userNameEditText.text.toString(),
                        phoneEditText.text.toString(),
                        emailEditText.text.toString()
                    )
                    val viewModel = (activity as UserActivity).userViewModel
                    if (args.newUser) { // in this case this is a new user, add it to the database
                        viewModel.addUserToDatabase(user)
                    } else { // in this case this is an exist user, update his data in the database
                        viewModel.updateUserInDatabase(user)
                    }
                    findNavController().navigateUp()
                }
                return true
            }
            android.R.id.home -> findNavController().navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    private fun setUserImageOnClickListener() = userImageView.setOnClickListener {
        // the user image is a random image from rickandmortyapi.com
        val imageNum = Random.nextInt(670)
        val randomImageUrl = "https://rickandmortyapi.com/api/character/avatar/$imageNum.jpeg"
        userImageView.tag = randomImageUrl // save the image url in the image view tag
        Glide.with(this)
            .load(randomImageUrl).apply(RequestOptions.circleCropTransform())
            .error(R.drawable.n_a)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(userImageView)
    }

    private fun setViewMode(viewMode: Boolean) {
        userImageView.isClickable = !viewMode
        imageInstructionsTextView.visibility = if (viewMode) View.INVISIBLE else View.VISIBLE
        userNameEditText.isEnabled = !viewMode
        phoneEditText.isEnabled = !viewMode
        emailEditText.isEnabled = !viewMode
    }

    private fun setUserDetails() {
        Glide.with(this)
            .load(args.userImage) // image url
            .error(R.drawable.n_a).apply(RequestOptions.circleCropTransform())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(userImageView)
        userImageView.tag = args.userImage
        userNameEditText.setText(args.userName)
        phoneEditText.setText(args.userPhone)
        emailEditText.setText(args.userEmail)
    }

    private fun areFieldsValid(): Boolean {
        val errorMessage = when {
            userImageView.tag == null -> getString(R.string.imageInstruction)
            userNameEditText.text.isNullOrEmpty() -> getString(R.string.errorMessage, userNameTextView.text)
            !checkFieldValidation(phoneEditText.text.toString(), Patterns.PHONE) -> getString(R.string.errorMessage, phoneTextView.text)
            !checkFieldValidation(emailEditText.text.toString(), Patterns.EMAIL_ADDRESS) -> getString(R.string.errorMessage, emailTextView.text)
            else -> null
        }
        return if (errorMessage != null) {
            AlertDialogFragment().apply {
                arguments = Bundle()
                arguments?.putString(AlertDialogFragment.MESSAGE_KEY, errorMessage)
            }.show(childFragmentManager, null)
            false
        } else {
            true
        }
    }

    private fun checkFieldValidation(fieldText: String?, pattern: Pattern) =
        !fieldText.isNullOrEmpty() && pattern.matcher(fieldText).matches()

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        view?.let { v ->
            val inputMethodService = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodService?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }


    class AlertDialogFragment : DialogFragment() {
        companion object {
            const val MESSAGE_KEY = "messageKey"
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val message = arguments?.getString(MESSAGE_KEY) ?: getString(R.string.error)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.error).setMessage(message)
                .setPositiveButton(getString(R.string.ok)) { _, _ -> dismiss() }

            return alertDialog.create()
        }
    }
}
