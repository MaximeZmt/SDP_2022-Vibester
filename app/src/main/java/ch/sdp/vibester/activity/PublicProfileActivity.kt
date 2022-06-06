package ch.sdp.vibester.activity

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
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.helper.ProfileInterface
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
class PublicProfileActivity : AppCompatActivity(), OnItemClickListener, ProfileInterface {
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

    private lateinit var userId: String

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

        setRetToMainBtnListener()
        setFollowingScoresBtnListener(R.id.profile_scores, R.id.profile_scroll_stat, R.id.profile_scroll_following)
        setFollowingScoresBtnListener(R.id.profile_following, R.id.profile_scroll_following, R.id.profile_scroll_stat)

        queryDatabase()
    }

    override fun queryDatabase() {
        dataGetter.getUserData(userId, this::setupProfile)
    }

    override fun setupRecycleViewForFriends() {
        findViewById<RecyclerView>(R.id.profile_followingList).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = followings?.let { ProfileFollowingAdapter(it, dataGetter, authenticator,this@PublicProfileActivity) }
            setHasFixedSize(true)
        }
    }

    override fun showFriendsPosition(friends: MutableList<User>?) {
        profileFollowingAdapter = ProfileFollowingAdapter(friends!!, dataGetter, authenticator, this)
        findViewById<RecyclerView>(R.id.profile_followingList)!!.adapter = profileFollowingAdapter
    }


    //check the UID here not sure
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResultHelper(requestCode, resultCode, data, imageGetter, dataGetter)
    }


    /**
     * Generic listener for the return to main button.
     */
    private fun setRetToMainBtnListener() {
        findViewById<FloatingActionButton>(R.id.profile_returnToMain).setOnClickListener {
            finish()
        }
    }

    override fun setFollowingScoresBtnListener(btnId: Int, show: Int, hide: Int) {
        findViewById<Button>(btnId).setOnClickListener {
            showAHideB(show, hide)
        }
    }

    override fun showAHideB(a: Int, b: Int) {
        findViewById<NestedScrollView>(a).visibility = View.VISIBLE
        findViewById<NestedScrollView>(b).visibility = View.GONE
    }

    override fun setImage(imageURI: Uri) {
        val avatar = findViewById<ImageView>(R.id.profile_image_ImageView)
        Helper().setImage(imageURI, avatar, imageSize)
    }


    override fun setTextOfView(id: Int, text: Int) {
        findViewById<TextView>(id).text = text.toString()
    }

    override fun setupProfile(user: User){
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