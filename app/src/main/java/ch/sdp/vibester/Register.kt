package ch.sdp.vibester

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import ch.sdp.vibester.auth.FireBaseAuthenticator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

//    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var authenticator: FireBaseAuthenticator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_log_in)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        authenticator = FireBaseAuthenticator()



        val btCreateAcc = findViewById<Button>(R.id.createAcc)
        val btLogIn = findViewById<Button>(R.id.logIn)
        val googleSignIn = findViewById<Button>(R.id.googleBtn)


        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val email = findViewById<TextView>(R.id.email)

        btCreateAcc.setOnClickListener {
            createAccount(username.text.toString(), password.text.toString(), email)
        }

        btLogIn.setOnClickListener {
            signIn(username.text.toString(), password.text.toString(), email)
        }

        googleSignIn.setOnClickListener {
            signInGoogle()
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = authenticator.auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                val email = findViewById<TextView>(R.id.email)
                email.text = account.email
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun signInGoogle() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, 1000)
    }

    private fun createAccount(email: String, password: String, emailText: TextView) {
        // [START create_user_with_email]
        authenticator.createAccount(email, password)
            .addOnCompleteListener(this) { task ->
                onCompleteSignIn(task, emailText)
            }
    }

    private fun signIn(email: String, password: String, emailText: TextView) {
        // [START sign_in_with_email]
        authenticator.signIn(email, password)
            .addOnCompleteListener(this) { task ->
                onCompleteSignIn(task, emailText)
            }
        // [END sign_in_with_email]
    }

    private fun onCompleteSignIn(task: Task<AuthResult>, emailText: TextView) {
        if (task.isSuccessful) {
            Toast.makeText(
                baseContext, "You have logged in successfully",
                Toast.LENGTH_SHORT
            ).show()
            val user = authenticator.auth.currentUser
            updateUI(user, emailText)
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.exception)
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
            updateUI(null, emailText)
        }
    }

    private fun reload() {

    }

    private fun updateUI(user: FirebaseUser?, emailText: TextView) {
        if (user != null) {
            emailText.text = user.email
        }
        else {
            emailText.text = "Authentication error"
        }
    }
}