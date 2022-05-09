package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.TestMode
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.IntentSwitcher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    private val AUTHENTICATION_PERMISSION_CODE = 1000

    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    private lateinit var auth: FirebaseAuth

    private lateinit var email: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText

    /**
     * Generic onCreate method, automatically called upon the creation of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_authentication)

        val googleSignInToken = "7687769601-qiqrp6kt48v89ub76k9lkpefh9ls36ha.apps.googleusercontent.com"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleSignInToken)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        email = findViewById(R.id.authentication_status)
    }

    /**
     * Listener bound to the "Create Account" button in the Authentication activity.
     */
    fun createAccountListener(view: View) {
        authenticate(username.text.toString(), password.text.toString(), true)
    }

    /**
     * Listener bound to the "Log In" button in the Authentication activity.
     */
    fun logInListener(view: View) {
        authenticate(username.text.toString(), password.text.toString(), false)
    }

    /**
     * Listener bound to the "Google Log In" button in the Authentication activity.
     */
    fun googleSignInListener(view: View) {
        signInGoogle()
    }

    /**
     * Listener bound to the red return button in the Authentication activity.
     */
    fun returnToMainListener(view: View) {
        IntentSwitcher.switch(this, WelcomeActivity::class.java, null)
        finish()
    }

    /**
     * Generic onStart method. Direct call to super.onStart().
     */
    public override fun onStart() {
        super.onStart()
    }

    /**
     * A function that is called on google sign in result
     * @param requestCode a request code
     * @param resultCode a result code
     * @param data intent returned from google sign in
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHENTICATION_PERMISSION_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(getString(R.string.log_tag), "firebaseAuthWithGoogle:" + account.id)
                googleAuthFirebase(account.idToken!!)
            } catch (e: ApiException) {
                Log.d(getString(R.string.log_tag), "Google sign in failed", e)
                updateUI("Authentication Error", false, null)
            }
        }
    }

    /**
     * Function that authenticates the given credentials with the ones stored on the Firebase.
     * @param idToken : String that holds the id token.
     */
    private fun googleAuthFirebase(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(getString(R.string.log_tag), "signInWithCredential:success")
                    var createAcc: Boolean = false
                    if (task.getResult().additionalUserInfo != null) {
                        createAcc = task.getResult().additionalUserInfo!!.isNewUser
                    }
                    val user = auth.currentUser
                    if (user != null) {
                        updateUI(user.email.toString(), createAcc, user)
                    }
                } else {
                    // fail
                    Log.d(getString(R.string.log_tag), "signInWithCredential:failure", task.exception)
                    updateUI("Authentication Error", false, null)
                }
            }
    }


    /**
     * A function validates email and password
     * @param email email
     * @param password passwprd
     * @return validity of email and password
     */
    private fun stringValidation(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            email.setText(R.string.emptyField)
            return false
        }

        if (!username.contains('@')) {
            email.setText(R.string.notAnEmail)
            return false
        }

        if (password.length < 6) {
            email.setText(R.string.shortPassword)
            return false
        }
        return true
    }

    /**
     * A function that authenticates the user
     * @param email email of the user
     * @param password password of the user
     * @param createAcc boolean to check if the authentication is a login or account creation
     */
    private fun authenticate(email: String, password: String, createAcc: Boolean) {
        if (stringValidation(email, password)) {
            var auth: Task<AuthResult> = if (createAcc) {
                authenticator.createAccount(email, password)
            }else {
                authenticator.signIn(email, password)
            }
            auth.addOnCompleteListener(this) { task ->
                onCompleteAuthentication(task, createAcc)
            }
        }
    }

    /**
     * A function that launches google sing in activity
     */
    private fun signInGoogle() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, AUTHENTICATION_PERMISSION_CODE)
    }

    /**
     * A function changes the UI based on the authentication result
     * @param task : The result
     * @param createAcc : Boolean to indicate the creation of an account instead of login
     */
    private fun onCompleteAuthentication(task: Task<AuthResult>, createAcc: Boolean) {
        if (task.isSuccessful) {
            Toast.makeText(
                baseContext, "You have logged in successfully",
                Toast.LENGTH_SHORT
            ).show()
            val user = authenticator.getCurrUser()
            if (user != null) {
                updateUI(user.email, createAcc, user)
            }
        } else {
            Toast.makeText(
                baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
            updateUI("Authentication error", false, null)
        }
    }

    /**
     * Function that allows the creation of a new activity, with the given text field as intent and
     * the given class as the destination.
     * @param email : The email to write
     * @param arg : The activity class to start
     */
    private fun startNewCustomActivity(email: String, arg: Class<*>?) {
        val newIntent = Intent(this, arg)
        newIntent.putExtra("email", email)
        startActivity(newIntent)
    }

    /**
     * Function that creates a CreateProfileActivity with the given string as the "email"
     * intent extra.
     * @param email : The email to write
     */
    private fun startNewActivity(email: String) {
        startNewCustomActivity(email, CreateProfileActivity::class.java)
    }

    /**
     * Function that updates the Authentication Activity's UI, mainly the textViews, according
     * to the given string, boolean and user arguments.
     * @param emailText : The email to write
     * @param createAcc : Boolean indicating the creation of an account instead of login
     * @param user      : The user
     */
    private fun updateUI(
        emailText: String?,
        createAcc: Boolean,
        user: FirebaseUser?
    ) {
        if (emailText != null) {
            if('@' in emailText && !createAcc) {
                startNewCustomActivity(emailText, ProfileActivity::class.java)
            }

            else if('@' in emailText && createAcc && user != null) { //
                if (TestMode.isTest()) {
                    startNewActivity(emailText)
                }else{
                    dataGetter.createUser(emailText, user.uid, this::startNewActivity, user.uid)
                }
            }

            else {
                email.text = emailText
            }
        }
    }
}