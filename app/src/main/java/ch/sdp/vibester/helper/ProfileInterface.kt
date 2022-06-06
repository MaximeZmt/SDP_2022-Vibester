package ch.sdp.vibester.helper

import android.net.Uri
import android.view.View
import ch.sdp.vibester.R
import ch.sdp.vibester.user.ProfileFollowingAdapter
import ch.sdp.vibester.user.User

interface ProfileInterface {
    val imageSize: Int
    val imageRequestCode: Int

    var followings: MutableList<User>?
    var profileFollowingAdapter: ProfileFollowingAdapter?

    fun queryDatabase()

    fun setupRecycleViewForFriends()

    fun showFriendsPosition(friends: MutableList<User>?)

    /**
     * @param btnId Id of the button (either following or scores)
     * @param show id of the view to show
     * @param hide id of the view to hide
     */
    fun setFollowingScoresBtnListener(btnId: Int, show: Int, hide: Int)

    /**
     * @param a id of the view to show
     * @param b id of the view to hide
     */
    fun showAHideB(a: Int, b: Int)

    /**
     * A function that downloads an image and sets it.
     * @param imageURI URI of the image
     */
    fun setImage(imageURI: Uri)

    /**
     * Function that sets the text of a given TextView to the string variant of an integer.
     * @param id: The id of the TextView to be changed.
     * @param text: The integer to be set as the text.
     */
    fun setTextOfView(id: Int, text: Int)

    /**
     * Function that sets the texts of multiple TextViews.
     * @param user: The user from which we will recover the text to set.
     */
    fun setTextOfMultipleViews(user: User) {
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
    fun setupProfile(user: User)

    /**
     * generate the qr code bitmap of the given data
     * @param data Qr Code data
     */
    fun generateQrCode(data: String)

    /**
     * helper function to add user from followingMap to followings
     * @param followingMap user.following
     *
     */
    fun loadFollowing(followingMap: Map<String, Boolean>)

    /**
     * callback function to add one user to followings
     * @param following the user to add in list
     */
    fun addFollowing(following: User) {
        followings?.add(following)
    }

    /**
     * Sets the given view's visibility.
     * @param view: The given view to modify.
     * @param isVisible: The indicator of which visibility to choose.
     * True for VISIBLE, false for GONE.
     */
    fun setViewVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}