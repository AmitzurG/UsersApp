
- Using Jetpack Room to save the users data persistently.
- The user entity contains "marked" property that isn't saved in the database (@Ignore), this property is used to mark users for delete (currently. In future it possible to add other actions on marked users). 
- The operations on the database are done in background, using coroutines and suspend functions, the view observes the data changes using LiveData (the list fragment observes the user list).
- The views access the data and operate on it by ViewModel (MVVM). 
- Using Glide library for image handling.

- The application shows the users list, for each user displaying her image and her name.
- There is "+" floating action button (on the bottom right corner of the screen) for add a new user, clicking on it is open a new fragment with the new user details fields to fill.
- Clicking on a user on the list is open the above fragment in view mode (impossible to edit the user details).
- Using JetPack Navigtion to navigate between the list fragment and the user details fragment.
- Each user row in the users list screen has a check box, there is "Delete" action on the toolbar, clicking on the "Delete" action deletes all the users that thier check box is checked.

- On the toolbar of the user details screen there is action which is "Save" in case of adding new user, it is "Edit" in case of exist user. 
- In case of open the details screen for exist user, clicking on "Edit" action (on the toolbar) changes the action to "Save" and moves the details form to edit mode (possible to edit the fields).
- In the details user screen, clicking on "Save" action causes to add a user in case of adding new user, or updates the user in case of existing user.
- In the details user screen, clicking on "back" cancels the addind/updating user.


