package ch.sdp.vibester

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class googleLogIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_log_in)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val btCreateAcc = findViewById<Button>(R.id.createAcc)
        val btLogIn = findViewById<Button>(R.id.logIn)


        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val email = findViewById<TextView>(R.id.email)

        btCreateAcc.setOnClickListener {
            createAccount(username.text.toString(), password.text.toString(), email)
        }

        btLogIn.setOnClickListener {
            signIn(username.text.toString(), password.text.toString(), email)
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun createAccount(email: String, password: String, emailText: TextView) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "You have created an acc successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    updateUI(user, emailText)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, task.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null, emailText)
                }
            }
    }

    private fun signIn(email: String, password: String, emailText: TextView) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "You have logged in successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    updateUI(user, emailText)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END sign_in_with_email]
    }


        private fun reload() {

    }

    private fun updateUI(user: FirebaseUser?, emailText: TextView) {
        if (user != null) {
            emailText.text = user.email
        }
    }
}