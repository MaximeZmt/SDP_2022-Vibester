package ch.sdp.vibester.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.PublicProfileActivity
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.helper.ProfileInterface
import ch.sdp.vibester.helper.ViewModel
import ch.sdp.vibester.user.OnItemClickListener
import ch.sdp.vibester.user.ProfileFollowingAdapter
import ch.sdp.vibester.user.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.AndroidEntryPoint
import net.glxn.qrgen.android.MatrixToImageWriter
import javax.inject.Inject

/** profile page of the current user with editable information */
@AndroidEntryPoint
class MyProfileFragment : Fragment(R.layout.activity_profile), OnItemClickListener, ProfileInterface {
    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    @Inject
    lateinit var imageGetter: ImageGetter

    override val imageSize = 1000
    override val imageRequestCode = 100

    override var followings: MutableList<User> ? = null
    override var profileFollowingAdapter: ProfileFollowingAdapter?= null
    private var vmMyProfile = ViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!authenticator.isLoggedIn()) {
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.main_bottom_nav_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.fragment_auth)
        }

        vmMyProfile.view = view
        vmMyProfile.ctx = view.context
        setupRecycleViewForFriends()
        followings = ArrayList()

        setFollowingScoresBtnListener(R.id.profile_scores, R.id.profile_scroll_stat, R.id.profile_scroll_following)
        setFollowingScoresBtnListener(R.id.profile_following, R.id.profile_scroll_following, R.id.profile_scroll_stat)

        queryDatabase()

        setViewVisibility(view.findViewById(R.id.editUser), true)
        setViewVisibility(view.findViewById(R.id.showQRCode), true)
        setViewVisibility(view.findViewById(R.id.logout), true)
        setViewVisibility(view.findViewById(R.id.profile_returnToMain), false)

        setEditUserNameBtnListener()
        setChangeImageBtnListener()
        setLogOutBtnListener()
        setShowQrCodeBtnListener()
        setQrCodeToProfileBtnListener()
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
        vmMyProfile.view.findViewById<ImageView>(R.id.editUser).setOnClickListener {
            showGeneralDialog( "username", true)
        }
    }

    /**
     * Generic listener for the change profile picture.
     * NOTES: we need to set both for both the cases where the user profile image is displayed or not
     */
    private fun setChangeImageBtnListener() {
        vmMyProfile.view.findViewById<ImageView>(R.id.profile_image_ImageView).setOnClickListener {
            showDialogWhenChangeImage()
        }
        vmMyProfile.view.findViewById<CardView>(R.id.profile_image_CardView).setOnClickListener {
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
        vmMyProfile.view.findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.fragment_auth)
        }
    }


    /**
     * Generic listener for the show qr code button.
     */
    private fun setShowQrCodeBtnListener() {
        vmMyProfile.view.findViewById<ImageView>(R.id.showQRCode).setOnClickListener {
            setViewVisibility(vmMyProfile.view.findViewById<ConstraintLayout>(R.id.QrCodePage), true)
            setViewVisibility(vmMyProfile.view.findViewById<RelativeLayout>(R.id.profileContent), false)
        }
    }

    /**
     * Generic listener for the show qr code and return to profile button.
     */
    private fun setQrCodeToProfileBtnListener() {
        vmMyProfile.view.findViewById<FloatingActionButton>(R.id.qrCode_returnToProfile).setOnClickListener {
            setViewVisibility(vmMyProfile.view.findViewById<ConstraintLayout>(R.id.QrCodePage), false)
            setViewVisibility(vmMyProfile.view.findViewById<RelativeLayout>(R.id.profileContent), true)
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
        val builder: AlertDialog.Builder = AlertDialog.Builder(vmMyProfile.ctx)
        builder.setTitle(title)

        val input = EditText(vmMyProfile.ctx)
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.id = id

        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            vmMyProfile.view.findViewById<TextView>(textId).text = input.text.toString()

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
        val builder: AlertDialog.Builder = AlertDialog.Builder(vmMyProfile.ctx)
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

    override fun setupRecycleViewForFriends() {
        vmMyProfile.view.findViewById<RecyclerView>(R.id.profile_followingList).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = followings?.let { ProfileFollowingAdapter(it, dataGetter, authenticator,this@MyProfileFragment) }
            setHasFixedSize(true)
        }
    }

    override fun showFriendsPosition(friends: MutableList<User>?) {
        profileFollowingAdapter = ProfileFollowingAdapter(friends!!, dataGetter, authenticator, this@MyProfileFragment)
        vmMyProfile.view.findViewById<RecyclerView>(R.id.profile_followingList)!!.adapter = profileFollowingAdapter
    }


    //check the UID here not sure
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            imageGetter.uploadFile("profileImg/${dataGetter.getCurrentUser()?.uid}", data?.data!!) {
                imageGetter.fetchImage("profileImg/${dataGetter.getCurrentUser()?.uid}", this::setImage)
            }
        }
    }

    override fun setFollowingScoresBtnListener(btnId: Int, show: Int, hide: Int) {
        vmMyProfile.view.findViewById<Button>(btnId).setOnClickListener {
            showAHideB(show, hide)
        }
    }

    override fun showAHideB(a: Int, b: Int) {
        vmMyProfile.view.findViewById<NestedScrollView>(a).visibility = View.VISIBLE
        vmMyProfile.view.findViewById<NestedScrollView>(b).visibility = View.GONE
    }

    override fun setImage(imageURI: Uri) {
        val avatar = vmMyProfile.view.findViewById<ImageView>(R.id.profile_image_ImageView)
        Helper().setImage(imageURI, avatar, imageSize)
    }


    override fun setTextOfView(id: Int, text: Int) {
        vmMyProfile.view.findViewById<TextView>(id).text = text.toString()
    }


    override fun setupProfile(user: User){
        // Currently assuming that empty username means no user !
        if (user.username != "") {
            vmMyProfile.view.findViewById<TextView>(R.id.username).text =  user.username
            setTextOfMultipleViews(user)
        }

        imageGetter.fetchImage("profileImg/${user.uid}", this::setImage)

        if (user.uid != "") {
            generateQrCode(user.uid)
        }

        if (user.following.isNotEmpty()) {
            loadFollowing(user.following)
        }

    }

    override fun loadFollowing(followingMap: Map<String, Boolean>) {
        followingMap.forEach { (userId, isFollowing) ->  if (isFollowing) dataGetter.getUserData(userId, this::addFollowing) }
        showFriendsPosition(followings)
    }

    override fun generateQrCode(data: String) {
        val size = 512
        val hints = HashMap<EncodeHintType?, Any?>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        val bmp = MatrixToImageWriter.toBitmap(bits)
        val qrCodeCanvas = Canvas(bmp)

        val scaleFactor = 4 // resize the image
        val logo = BitmapFactory.decodeStream(resources.assets.open("logo.png"))
        logo.density = logo.density * scaleFactor
        val xLogo = (size - logo.width / scaleFactor) / 2f
        val yLogo = (size - logo.height / scaleFactor) / 2f

        qrCodeCanvas.drawBitmap(logo, xLogo, yLogo, null)

        vmMyProfile.view.findViewById<ImageView>(R.id.qrCode).setImageBitmap(bmp)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(vmMyProfile.ctx, PublicProfileActivity::class.java)
        intent.putExtra("UserId", followings?.get(position)?.uid)
        startActivity(intent)
    }
}