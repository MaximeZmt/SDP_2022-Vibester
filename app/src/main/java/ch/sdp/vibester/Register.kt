package ch.sdp.vibester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import ch.sdp.vibester.auth.FireBaseAuthenticator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class Register : AppCompatActivity() {

//    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var authenticator: FireBaseAuthenticator

    private lateinit var email: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_log_in)

//    @FixMe
//      Commenting this for now until we find a proper way to test it, then will merge it to main

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()

//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        authenticator = FireBaseAuthenticator()

        val btCreateAcc = findViewById<Button>(R.id.createAcc)
        val btLogIn = findViewById<Button>(R.id.logIn)
//        val googleSignIn = findViewById<Button>(R.id.googleBtn)


        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        email = findViewById<TextView>(R.id.email)

        btCreateAcc.setOnClickListener {
            createAccount(username.text.toString(), password.text.toString())
        }

        btLogIn.setOnClickListener {
            signIn(username.text.toString(), password.text.toString())
        }

//        googleSignIn.setOnClickListener {
//            signInGoogle()
//        }


    }

    public override fun onStart() {
        super.onStart()
        val currentUser = authenticator.auth.currentUser
        if (currentUser != null) {
            reload();
        }
    }

//    @FixMe
//      Commenting this for now until we find a proper way to test it, then will merge it to main
//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        updateUI(authenticator.googleActivityResult(requestCode, resultCode, data));
//    }

//    private fun signInGoogle() {
//        val intent = googleSignInClient.signInIntent
//        startActivityForResult(intent, 1000)
//    }

    private fun createAccount(email: String, password: String) {
        authenticator.createAccount(email, password)
            .addOnCompleteListener(this) { task ->
                onCompleteAuthentication(task)
            }
    }

    private fun signIn(email: String, password: String) {
        authenticator.signIn(email, password)
            .addOnCompleteListener(this) { task ->
                onCompleteAuthentication(task)
            }
    }

    private fun onCompleteAuthentication(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            Toast.makeText(
                baseContext, "You have logged in successfully",
                Toast.LENGTH_SHORT
            ).show()
            val user = authenticator.auth.currentUser
            if (user != null) {
                updateUI(user.email)
            }
        } else {
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
            updateUI("Authentication error")
        }
    }

    private fun reload() {

    }

    private fun updateUI(emailText: String?) {
        email.text = emailText
    }
}