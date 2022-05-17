package ch.sdp.vibester.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TextView
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

/** profile page of a user with only public information */
class PublicProfileActivity @Inject constructor(private val ctx: Context,
                                                private val userId:String,
                                                private val scoreOrFriends: String,
                                                val imageGetter: ImageGetter,
                                                val dataGetter: DataGetter,
                                                val authenticator: FireBaseAuthenticator){

    lateinit var profileActivity:ProfileActivity
    private lateinit var alertDialog: Dialog

    /**
     * Remove/add elements in layout, set button listeners
     */
    private fun setDialogLayout(){
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (scoreOrFriends == "Scores") {
            alertDialog.findViewById<TableLayout>(R.id.profile_statistics)?.visibility = VISIBLE
        }
        alertDialog.findViewById<Button>(R.id.profile_logout)?.visibility = GONE
        alertDialog.findViewById<FloatingActionButton>(R.id.profile_returnToMain)?.visibility = GONE

        val close = alertDialog.findViewById<TextView>(R.id.profile_close)
        close.visibility = VISIBLE
        close.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.findViewById<ScrollView>(R.id.profileContent).layoutParams.width = ctx.resources.getDimensionPixelSize(R.dimen.profile_width);
        alertDialog.findViewById<ScrollView>(R.id.profileContent).layoutParams.height = ctx.resources.getDimensionPixelSize(R.dimen.profile_height);
    }

    /**
     * Set dialog and fetch data from db
     */
    fun setProfileDialog(){
        val dialogView = LayoutInflater.from(ctx).inflate(R.layout.activity_profile, null)
        alertDialog = Dialog(ctx)
        alertDialog.setContentView(dialogView)
        setDialogLayout()

        profileActivity = ProfileActivity(alertDialog.context, dialogView, imageGetter, dataGetter, authenticator)
        profileActivity.queryDatabase(userId)
        profileActivity.setScoreBtnListener()

        alertDialog.show()
    }

}