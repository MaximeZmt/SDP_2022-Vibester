package ch.sdp.vibester.activity

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.profile.UserProfile

class ProfileActivity : AppCompatActivity() {
    private val EXTRA_ID = "userProfile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val bundle = intent.extras;
        val user: UserProfile = bundle?.getSerializable(EXTRA_ID) as UserProfile
        setupProfile(user)

        val editUsername = findViewById<Button>(R.id.editUser)
        val editHandle = findViewById<Button>(R.id.editHandle)

        editUsername.setOnClickListener {
            showGeneralDialog(R.id.username, "username")
        }

        editHandle.setOnClickListener {
            showGeneralDialog(R.id.handle, "handle")
        }
    }



    private fun showDialog(title: String, hint: String, id: Int, textId: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        val input = EditText(this)
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.id = id

        builder.setView(input)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
            findViewById<TextView>(textId).text = input.text.toString()
        })
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showGeneralDialog(id: Int, name: String) {
        val title = "Create $name"
        val hint = "Enter new $name"

        showDialog(title, hint, 0, id)
    }


    private fun setupProfile(user: UserProfile){
        findViewById<TextView>(R.id.handle).text =  user.handle
        findViewById<TextView>(R.id.username).text = user.username
        findViewById<TextView>(R.id.totalGames).text = user.totalGames.toString()
        findViewById<TextView>(R.id.correctSongs).text = user.correctSongs.toString()
        findViewById<TextView>(R.id.bestScore).text = user.bestScore.toString()
        findViewById<TextView>(R.id.ranking).text = user.ranking.toString()
        /* TODO: add functionality to display the image (may be using )
        findViewById<ImageView>(R.id.avatar).loadImg(user.image)*/
    }
}

