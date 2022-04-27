package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    private lateinit var auth: FirebaseAuth

    private lateinit var email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_authentication)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("7687769601-qiqrp6kt48v89ub76k9lkpefh9ls36ha.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        val btCreateAcc = findViewById<Button>(R.id.createAcc)
        val btLogIn = findViewById<Button>(R.id.logIn)
        val googleSignIn = findViewById<Button>(R.id.googleBtn)

        val returnToMain = findViewById<FloatingActionButton>(R.id.authentication_returnToMain)

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

        returnToMain.setOnClickListener {
            IntentSwitcher.switchBackToWelcome(this)
            finish()
        }
    }

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
        if (requestCode == 1000) {
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


    private fun googleAuthFirebase(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(getString(R.string.log_tag), "signInWithCredential:success")
                    var createAcc: Boolean = false
                    if(task.getResult().additionalUserInfo != null){
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
        startActivityForResult(intent, 1000)
    }

    /**
     * A function changes the UI based on the authentication result
     * @param task result
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


//TODO
    private fun startNewActivity(email: String) {
        val newIntent = Intent(this, CreateProfileActivity::class.java)
        newIntent.putExtra("email", email)
        startActivity(newIntent)
    }


    private fun updateUI(
        emailText: String?,
        createAcc: Boolean,
        user: FirebaseUser?
    ) {
        if (emailText != null) {
            if('@' in emailText && !createAcc) {
                val newIntent = Intent(this, ProfileActivity::class.java)
                newIntent.putExtra("email", emailText)
                startActivity(newIntent)
            }

            else if('@' in emailText && createAcc && user != null) { //
                if(TestMode.isTest()){
                    startNewActivity(emailText)
                }else{
                    dataGetter.createUser(
                        emailText,
                        user.uid,
                        this::startNewActivity,
                        user.uid
                    )
                }
            }

            else {
                email.text = emailText
            }
        }
    }
}