package ch.sdp.vibester.activity.profile

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.MainActivity
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.helper.IntentSwitcher
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

/** profile page of the current user with editable information */
class MyProfileActivity : ProfileActivity() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!authenticator.isLoggedIn()) {
            val navController = findNavController()
            navController.navigate(R.id.fragment_authentication)
        }
        queryDatabase()

        setViewVisibility(view.findViewById(R.id.editUser), true)
        setViewVisibility(view.findViewById(R.id.showQRCode), true)
        setViewVisibility(view.findViewById(R.id.logout), true)

        setEditUserNameBtnListener()
        setChangeImageBtnListener()
        setLogOutBtnListener()
        setShowQrCodeBtnListener()
        setQrCodeToProfileBtnListener()

        setupRecycleViewForFriends()

        setFollowingScoresBtnListener(R.id.profile_scores, R.id.profile_scroll_stat, R.id.profile_scroll_following)
        setFollowingScoresBtnListener(R.id.profile_following, R.id.profile_scroll_following, R.id.profile_scroll_stat)

    }

    override fun queryDatabase() {
        val currentUser = authenticator.getCurrUser()
        if (currentUser != null) {
            dataGetter.getUserData(currentUser.uid, this::setupProfile)
        }
    }


    /**
     * Generic listener for the edit username button.
     */
    private fun setEditUserNameBtnListener() {
        requireView().findViewById<ImageView>(R.id.editUser).setOnClickListener {
            showGeneralDialog( "username", true)
        }
    }

    /**
     * Generic listener for the change profile picture.
     * NOTES: we need to set both for both the cases where the user profile image is displayed or not
     */
    private fun setChangeImageBtnListener() {
        requireView().findViewById<ImageView>(R.id.profile_image_ImageView).setOnClickListener {
            showDialogWhenChangeImage()
        }
        requireView().findViewById<CardView>(R.id.profile_image_CardView).setOnClickListener {
            showDialogWhenChangeImage()
        }
    }

    /**
     * helper function to show dialog 
     */
    private fun showDialogWhenChangeImage() {
        showGeneralDialog(R.string.profile_verify_change_profile_pic.toString(), false)
    }

    /**
     * A function that updates the image in the database.
     * @param id ID of the image int the database
     */
    private fun updateImage(id: String) {
        deleteImage(id)

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    }

    /**
     * A function that deletes an image from the database.
     * @param id ID of the image int the database
     */
    private fun deleteImage(id: String) {
        imageGetter.deleteImage("profileImg/${id}")
    }

    /**
     * Generic listener for the log out button.
     */
    private fun setLogOutBtnListener() {
        requireView().findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            IntentSwitcher.switch(requireContext(), MainActivity::class.java)
        }
    }


    /**
     * Generic listener for the show qr code button.
     */
    private fun setShowQrCodeBtnListener() {
        requireView().findViewById<ImageView>(R.id.showQRCode).setOnClickListener {
            setViewVisibility(requireView().findViewById<ConstraintLayout>(R.id.QrCodePage), true)
            setViewVisibility(requireView().findViewById<RelativeLayout>(R.id.profileContent), false)
        }
    }

    /**
     * Generic listener for the show qr code and return to profile button.
     */
    private fun setQrCodeToProfileBtnListener() {
        requireView().findViewById<FloatingActionButton>(R.id.qrCode_returnToProfile).setOnClickListener {
            setViewVisibility(requireView().findViewById<ConstraintLayout>(R.id.QrCodePage), false)
            setViewVisibility(requireView().findViewById<RelativeLayout>(R.id.profileContent), true)
        }
    }


    /**
     * Sets the given view's visibility.
     * @param view: The given view to modify.
     * @param isVisible: The indicator of which visibility to choose.
     * True for VISIBLE, false for GONE.
     */
    private fun setViewVisibility(view: View, isVisible: Boolean){
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }



    /**
     * A function that displays the dialog
     * @param title title of the dialog
     * @param hint hint of the text in the dialog
     * @param id id of the dialog
     * @param textId id of the text in the dialog
     * @param name of the dialog
     */
    private fun showTextDialog(title: String, hint: String, id: Int, textId: Int, name: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)

        val input = EditText(requireContext())
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.id = id

        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            requireView().findViewById<TextView>(textId).text = input.text.toString()

            if (name == "username"){
                dataGetter.setFieldValue(FireBaseAuthenticator().getCurrUID(), "username",  input.text.toString())
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    /**
     * A function shows an image change dialog.
     * @param title title of the dialog
     */
    private fun showImageChangeDialog(title: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)

        builder.setPositiveButton("Yes") { _, _ ->
            dataGetter.getCurrentUser()?.let { updateImage(it.uid) }
        }

        builder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    /**
     * A function shows a dialog.
     * @param name name of the dialog
     * @param textDialog boolean to check the type of dialog
     */
    private fun showGeneralDialog(name: String, textDialog: Boolean) {
        if (textDialog) {
            val title = "Create $name"
            val hint = "Enter new $name"

            showTextDialog(title, hint, 0, R.id.username, name)
        }
        else {
            showImageChangeDialog(name)
        }
    }
}