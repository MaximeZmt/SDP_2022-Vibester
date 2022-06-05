package ch.sdp.vibester.activity.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.MainActivity
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.helper.IntentSwitcher
import ch.sdp.vibester.user.OnItemClickListener
import ch.sdp.vibester.user.ProfileFollowingAdapter
import ch.sdp.vibester.user.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.AndroidEntryPoint
import net.glxn.qrgen.android.MatrixToImageWriter
import javax.inject.Inject

/** profile page of a user with only public information */
@AndroidEntryPoint
class PublicProfileActivity : AppCompatActivity(), OnItemClickListener {
    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    @Inject
    lateinit var imageGetter: ImageGetter

    private val imageSize = 1000
    private val imageRequestCode = 100

    private var followings: MutableList<User> ? = null
    private var profileFollowingAdapter: ProfileFollowingAdapter?= null

    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile)

        userId = intent.getStringExtra("UserId").toString()
        if (intent.getStringExtra("ScoresOrFollowing") == R.string.profile_scores.toString()) {
            showAHideB(R.id.profile_scroll_stat, R.id.profile_scroll_following)
        } else if (intent.getStringExtra("ScoresOrFollowing") == R.string.profile_following.toString()) {
            showAHideB(R.id.profile_scroll_following, R.id.profile_scroll_stat)
        }

        setupRecycleViewForFriends()
        followings = ArrayList()

        // TODO remove setRetToMainBtnListener()
        setFollowingScoresBtnListener(R.id.profile_scores, R.id.profile_scroll_stat, R.id.profile_scroll_following)
        setFollowingScoresBtnListener(R.id.profile_following, R.id.profile_scroll_following, R.id.profile_scroll_stat)

        queryDatabase()

    }

    fun queryDatabase() {
        dataGetter.getUserData(userId, this::setupProfile)
    }

    private fun setupRecycleViewForFriends() {
        findViewById<RecyclerView>(R.id.profile_followingList).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = followings?.let { ProfileFollowingAdapter(it, dataGetter, authenticator,this@PublicProfileActivity) }
            setHasFixedSize(true)
        }
    }

    private fun showFriendsPosition(friends: MutableList<User>?) {
        profileFollowingAdapter = ProfileFollowingAdapter(friends!!, dataGetter, authenticator, this)
        findViewById<RecyclerView>(R.id.profile_followingList)!!.adapter = profileFollowingAdapter
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

    /**
     * @param btnId Id of the button (either following or scores)
     * @param show id of the view to show
     * @param hide id of the view to hide
     */
    private fun setFollowingScoresBtnListener(btnId: Int, show: Int, hide: Int) {
        findViewById<Button>(btnId).setOnClickListener {
            showAHideB(show, hide)
        }
    }

    /**
     * @param a id of the view to show
     * @param b id of the view to hide
     */
    fun showAHideB(a: Int, b: Int) {
        findViewById<NestedScrollView>(a).visibility = View.VISIBLE
        findViewById<NestedScrollView>(b).visibility = View.GONE
    }

    /**
     * A function that downloads an image and sets it.
     * @param imageURI URI of the image
     */
    private fun setImage(imageURI: Uri) {
        val avatar = findViewById<ImageView>(R.id.profile_image_ImageView)
        Helper().setImage(imageURI, avatar, imageSize)
    }


    /**
     * Function that sets the text of a given TextView to the string variant of an integer.
     * @param id: The id of the TextView to be changed.
     * @param text: The integer to be set as the text.
     */
    private fun setTextOfView(id: Int, text: Int) {
        findViewById<TextView>(id).text = text.toString()
    }

    /**
     * Function that sets the texts of multiple TextViews.
     * @param user: The user from which we will recover the text to set.
     */
    private fun setTextOfMultipleViews(user: User) {
        setTextOfView(R.id.profile_total_games_stat, user.totalGames)
        setTextOfView(R.id.profile_top_tracks, user.scores.getOrDefault("top tracks", 0))
        setTextOfView(R.id.profile_kpop, user.scores.getOrDefault("kpop", 0))
        setTextOfView(R.id.profile_rock, user.scores.getOrDefault("rock", 0))
        setTextOfView(R.id.profile_bts, user.scores.getOrDefault("BTS", 0))
        setTextOfView(R.id.profile_imagine_dragons, user.scores.getOrDefault("Imagine Dragons", 0))
        setTextOfView(R.id.profile_billie_eilish, user.scores.getOrDefault("Billie Eilish", 0))
    }

    /**
     * Function to handle setting up the profile.
     * @param user: The user whose profile we are setting up.
     */
    fun setupProfile(user: User){
        // Currently assuming that empty username means no user !
        if (user.username != "") {
            findViewById<TextView>(R.id.username).text =  user.username
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

    /**
     * helper function to add user from followingMap to followings
     * @param followingMap user.following
     *
     */
    private fun loadFollowing(followingMap: Map<String, Boolean>) {
        followingMap.forEach { (userId, isFollowing) ->  if (isFollowing) dataGetter.getUserData(userId, this::addFollowing) }
        showFriendsPosition(followings)
    }

    /**
     * callback function to add one user to followings
     * @param following the user to add in list
     */
    private fun addFollowing(following: User) {
        followings?.add(following)
    }

    /**
     * generate the qr code bitmap of the given data
     * @param data Qr Code data
     */
    private fun generateQrCode(data: String) {
        val size = 512
        val hints = HashMap<EncodeHintType?, Any?>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        val bmp = MatrixToImageWriter.toBitmap(bits)
        val qrCodeCanvas = Canvas(bmp)

        val scaleFactor = 4 // resize the image
        val logo = BitmapFactory.decodeStream(assets.open("logo.png"))
        logo.density = logo.density * scaleFactor
        val xLogo = (size - logo.width / scaleFactor) / 2f
        val yLogo = (size - logo.height / scaleFactor) / 2f

        qrCodeCanvas.drawBitmap(logo, xLogo, yLogo, null)

        findViewById<ImageView>(R.id.qrCode).setImageBitmap(bmp)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, PublicProfileActivity::class.java)
        intent.putExtra("UserId", followings?.get(position)?.uid)
        startActivity(intent)
    }
}