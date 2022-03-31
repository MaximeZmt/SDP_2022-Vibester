package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var authenticator: FireBaseAuthenticator

    private lateinit var email: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_google_log_in)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        authenticator = FireBaseAuthenticator()

        val btCreateAcc = findViewById<Button>(R.id.createAcc)
        val btLogIn = findViewById<Button>(R.id.logIn)
        val googleSignIn = findViewById<Button>(R.id.googleBtn)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        email = findViewById(R.id.email)

        btCreateAcc.setOnClickListener {
            authenticate(username.text.toString(), password.text.toString(), true)
        }

        btLogIn.setOnClickListener {
            authenticate(username.text.toString(), password.text.toString(), false)
        }

        googleSignIn.setOnClickListener {
            signInGoogle()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = authenticator.auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    /**
     * A function to updates the UI based on google sign in result
     * @param requestCode a request code
     * @param resultCode a result code
     * @param data intent returned from google sign in
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateUI(authenticator.googleActivityResult(requestCode, resultCode, data))
    }

    /**
     * A function validates email and password
     * @param email email
     * @param password passwprd
     * @return validity of email and password
     */
    private fun stringValidation(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            email.text = "Empty email or password"
            return false
        }

        if (!username.contains('@')) {
            email.text = "Not an email"
            return false
        }

        if (password.length < 6) {
            email.text = "Password has to be at least 6 symbols"
            return false
        }
        return true
    }

    private fun authenticate(email: String, password: String, creatAcc: Boolean) {
        if (stringValidation(email, password)) {
            var auth: Task<AuthResult>
            if (creatAcc) {
                auth = authenticator.createAccount(email, password)
<<<<<<< HEAD
            }else {
=======
                //createAccount(email, password)
            } else {
>>>>>>> origin
                auth = authenticator.signIn(email, password)
            }
            auth.addOnCompleteListener(this) { task ->
                onCompleteAuthentication(task)
            }
        }
    }

    /**
     * A function that launches google sing in activity
     */
    private fun signInGoogle() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, 1000)
    }

    /**
     * A function changes the UI based on the authentication result
     * @param task result
     */
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
            Toast.makeText(
                baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
            updateUI("Authentication error")
        }
    }

    private fun reload() {

    }

    private fun updateUI(emailText: String?) {
        if (emailText != null) {
            if('@' in emailText) {
                val newIntent = Intent(this, ProfileActivity::class.java)
                newIntent.putExtra("email", emailText)
                startActivity(newIntent)
            }
            else {
                email.text = emailText
            }
        }
    }
}