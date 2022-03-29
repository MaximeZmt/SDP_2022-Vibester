package ch.sdp.vibester.activity

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.profile.UserProfile
import com.google.firebase.ktx.Firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private val EXTRA_ID = "userProfile"

    private var database: FirebaseDatabase = Firebase.database("https://vibester-sdp-default-rtdb.europe-west1.firebasedatabase.app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_profile)
        queryDatabase()

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
     */

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

    /**
     * A function that displays the dialog
     * @param title title of the dialog
     * @param hint hint of the text in the dialog
     * @param id id of the dialog
     * @param textId id of the text in the dialog
     */

    private fun showGeneralDialog(id: Int, name: String) {
        val title = "Create $name"
        val hint = "Enter new $name"

        showDialog(title, hint, 0, id)
    }

    /**
     * A function that queries the database and fetched the correct user
     * Hard coded for now
     */

    private fun queryDatabase() {
        var user: UserProfile
        val userRef = database.getReference("users")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapShot in dataSnapshot.children) {
                    val dbContents: Map<String, Objects> = dataSnapShot.value as Map<String, Objects>
                    user = UserProfile(
                        dbContents["handle"].toString(),
                        dbContents["username"].toString(),
                        dbContents["image"].toString(),
                        dbContents["totalGames"].toString().toInt(),
                        dbContents["bestScore"].toString().toInt(),
                        dbContents["correctSongs"].toString().toInt())
                    setupProfile(user)
                    break
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadUsers:onCancelled", databaseError.toException())
            }
        })
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

