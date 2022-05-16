package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.IntentSwitcher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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

    private lateinit var authentication_status: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText

    private var createAcc = false

    /**
     * Generic onCreate method, automatically called upon the creation of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
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
        authentication_status = findViewById(R.id.authentication_status)
    }

    /**
     * Listener bound to the "Create Account" button in the Authentication activity.
     */
    fun createAccountListener(view: View) {
        this.createAcc = true
        authenticate(username.text.toString(), password.text.toString())
    }

    /**
     * Listener bound to the "Log In" button in the Authentication activity.
     */
    fun logInListener(view: View) {
        authenticate(username.text.toString(), password.text.toString())
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
        IntentSwitcher.switch(this, MainActivity::class.java)
        finish()
    }

    /**
     * Generic onStart method. Direct call to super.onStart().
     */
    public override fun onStart() {
        super.onStart()
    }


    /**
     * GoogleSignIn result
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
                updateOnFail()
            }
        }
    }

    /**
     * Launch GoogleSingIn activity
     */
    private fun signInGoogle() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, AUTHENTICATION_PERMISSION_CODE)
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
                    if (task.getResult().additionalUserInfo != null) {
                        createAcc = task.getResult().additionalUserInfo!!.isNewUser
                        createAccount()
                    }
                    updateOnSuccess()
                } else {
                    // fail
                    Log.d(getString(R.string.log_tag), "signInWithCredential:failure", task.exception)
                    updateOnFail()
                }
            }
    }

    /**
     * A function validates email and password
     * @param email email
     * @param password passwprd
     * @return validity of email and password
     */
    private fun credentialsValidation(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            authentication_status.setText(R.string.emptyField)
            return false
        }

        if (!username.contains('@')) {
            authentication_status.setText(R.string.notAnEmail)
            return false
        }

        if (password.length < 6) {
            authentication_status.setText(R.string.shortPassword)
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
    private fun authenticate(email: String, password: String) {
        if (credentialsValidation(email, password)) {
            val auth: Task<AuthResult> = if (createAcc) {
                authenticator.createAccount(email, password)
            }else {
                authenticator.signIn(email, password)
            }

            auth.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if(createAcc)createAccount()
                    updateOnSuccess()
                } else {
                    updateOnFail()
                }
            }
        }
    }


    /**
     * Function that allows the creation of a new activity, with the given text field as intent and
     * the given class as the destination.
     * @param email : The email to write
     * @param arg : The activity class to start
     */
    private fun startNewCustomActivity() {
        val newIntent = Intent(this,ProfileActivity::class.java)
        startActivity(newIntent)
    }

    private fun createAccount(){
        dataGetter.createUser(authenticator.getCurrUserMail(), "fake", this::startNewCustomActivity, authenticator.getCurrUID())
    }

    private fun updateOnSuccess(){
        Toast.makeText(baseContext, "You have logged in successfully", Toast.LENGTH_SHORT).show()
        startNewCustomActivity()
    }

    private fun updateOnFail(){
        Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
    }
    
}