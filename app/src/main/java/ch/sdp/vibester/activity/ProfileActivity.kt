package ch.sdp.vibester.activity

import android.os.Bundle
import android.text.InputType
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.api.BitmapGetterApi
import ch.sdp.vibester.model.UserSharedPref
import ch.sdp.vibester.database.UsersRepo
import ch.sdp.vibester.profile.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    @Inject
    lateinit var usersRepo: UsersRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_profile)
        var email = intent.getStringExtra("email").toString()

        //queryDatabase(email)

        setupProfile(UserSharedPref.getUser(this))

        val editUsername = findViewById<Button>(R.id.editUser)
        val editHandle = findViewById<Button>(R.id.editHandle)

        editUsername.setOnClickListener {
            showGeneralDialog(R.id.username, "username")
        }

        editHandle.setOnClickListener {
            showGeneralDialog(R.id.handle, "handle")
        }

    }

    /**
     * A function that displays the dialog
     * @param title title of the dialog
     * @param hint hint of the text in the dialog
     * @param id id of the dialog
     * @param textId id of the text in the dialog
     * @param name of the dialog
     */

    private fun showDialog(title: String, hint: String, id: Int, textId: Int, name: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        val input = EditText(this)
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.id = id

        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            findViewById<TextView>(textId).text = input.text.toString()

            //usersRepo.updateField("-Myfy9TlCUTWYRxVLBsQ", input.text.toString(), name)

            if(name == "username"){
                UserSharedPref.updateUsername(this, input.text.toString())
            }else if (name == "handle"){
                UserSharedPref.updateHandle(this, input.text.toString())
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    /**
     * A function that displays the dialog
     */
    private fun showGeneralDialog(id: Int, name: String) {
        val title = "Create $name"
        val hint = "Enter new $name"

        showDialog(title, hint, 0, id, name)
    }

    /**
     * A function that queries the database and fetched the correct user
     * Hard coded for now
     */

    private fun queryDatabase(email: String) {
        usersRepo.getUserData(email, this::setupProfile)

    }


    private fun setupProfile(user: UserProfile){
        findViewById<TextView>(R.id.handle).text =  user.handle
        findViewById<TextView>(R.id.username).text = user.username
        findViewById<TextView>(R.id.totalGames).text = user.totalGames.toString()
        findViewById<TextView>(R.id.correctSongs).text = user.correctSongs.toString()
        findViewById<TextView>(R.id.bestScore).text = user.bestScore.toString()
        findViewById<TextView>(R.id.ranking).text = user.ranking.toString()
        CoroutineScope(Dispatchers.Main).launch {
            val task = async(Dispatchers.IO) {
                try {
                    val bit = BitmapGetterApi.download("https://" + user.image)
                    bit.get()
                }catch (e: Exception){
                    null
                }
            }
            val bm = task.await()
            findViewById<ImageView>(R.id.avatar).setImageBitmap(bm)
        }
    }
}

