package ch.sdp.vibester.profile

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ch.sdp.vibester.R
const val EXTRA_ID = "userProfile"

class ProfileSetup: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val bundle = intent.extras;
        val user: UserProfile = bundle?.getSerializable(EXTRA_ID) as UserProfile
        setupProfile(user)

        val editUsername = findViewById<Button>(R.id.editProfile)

        editUsername.setOnClickListener {
            showdialog()
        }
    }

    private fun showdialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Title")

// Set up the input
        val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter Text")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

// Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            findViewById<TextView>(R.id.username).text = input.text.toString()
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun setupProfile(user: UserProfile){
        findViewById<TextView>(R.id.handle).text =  user.handle
        findViewById<TextView>(R.id.username).text = user.username
        findViewById<TextView>(R.id.totalGames).text = user.totalGames.toString()
        findViewById<TextView>(R.id.correctSongs).text = user.correctSongs.toString()
        findViewById<TextView>(R.id.bestScore).text = user.bestScore.toString()
        findViewById<TextView>(R.id.ranking).text = user.ranking.toString()
        /* TODO: add functionality to display the image
        findViewById<ImageView>(R.id.avatar).loadImg(user.image)*/
    }
}

