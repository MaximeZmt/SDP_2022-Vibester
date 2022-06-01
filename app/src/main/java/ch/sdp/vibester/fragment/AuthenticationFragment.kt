package ch.sdp.vibester.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.helper.ViewModel
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
import net.datafaker.Faker

@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_layout_authentication) {
    private val AUTHENTICATION_PERMISSION_CODE = 1000

    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var dataGetter: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    private val faker: Faker = Faker()

    private lateinit var auth: FirebaseAuth

    private lateinit var authentication_status: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private var vmAuth = ViewModel()

    private var createAcc = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmAuth.view = view
        vmAuth.ctx = view.context
        val googleSignInToken =
            "7687769601-qiqrp6kt48v89ub76k9lkpefh9ls36ha.apps.googleusercontent.com"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleSignInToken)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(vmAuth.ctx, gso)

        auth = Firebase.auth

        username = vmAuth.view.findViewById(R.id.username)
        password = vmAuth.view.findViewById(R.id.password)
        authentication_status = vmAuth.view.findViewById(R.id.authentication_status)

        vmAuth.view.findViewById<Button>(R.id.createAcc)
            .setOnClickListener { createAccountListener() }
        vmAuth.view.findViewById<Button>(R.id.logIn).setOnClickListener { logInListener() }
        vmAuth.view.findViewById<Button>(R.id.googleBtn)
            .setOnClickListener { googleSignInListener() }

    }

    /**
     * Listener bound to the "Create Account" button in the Authentication activity.
     */
    private fun createAccountListener() {
        this.createAcc = true
        authenticate(username.text.toString(), password.text.toString())
    }

    /**
     * Listener bound to the "Log In" button in the Authentication activity.
     */
    private fun logInListener() {
        authenticate(username.text.toString(), password.text.toString())
    }

    /**
     * Listener bound to the "Google Log In" button in the Authentication activity.
     */
    private fun googleSignInListener() {
        signInGoogle()
    }

    /**
     * Listener bound to the "Reset Password" button in the Authentication activity.
     */
    fun resetPasswordListener() {
        resetPassword(username.text.toString())
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
                updateOnFail("Google SignIn authentication failed")
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
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(getString(R.string.log_tag), "signInWithCredential:success")
                    if (task.getResult().additionalUserInfo != null) {
                        createAcc = task.getResult().additionalUserInfo!!.isNewUser
                        createAccount()
                    }
                    updateOnSuccess()
                } else {
                    Log.d(
                        getString(R.string.log_tag),
                        "signInWithCredential:failure",
                        task.exception
                    )
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
            authentication_status.setText(R.string.authentication_emptyField)
            return false
        }

        if (!username.contains('@')) {
            authentication_status.setText(R.string.authentication_notAnEmail)
            return false
        }

        if (password.length < 6) {
            authentication_status.setText(R.string.authentication_shortPassword)
            return false
        }
        return true
    }


    /**
     * A function that authenticates the user
     * @param email email of the user
     * @param password password of the user
     */
    private fun authenticate(email: String, password: String) {
        if (credentialsValidation(email, password)) {
            val auth: Task<AuthResult> = if (createAcc) {
                authenticator.createAccount(email, password)
            } else {
                authenticator.signIn(email, password)
            }

            auth.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (createAcc) createAccount()
                    updateOnSuccess()
                } else {
                    updateOnFail()
                }
            }
        }
    }

    private fun startProfileActivity() {
        findNavController().popBackStack()
    }

    /**
     * Random username generator with Faker
     */
    private fun usernameGenerator(): String {
        return faker.color().name() + "." + faker.animal().name()
    }

    /**
     * Create an account with new email, uid and random username
     */
    private fun createAccount() {
        val username = usernameGenerator()
        dataGetter.createUser(authenticator.getCurrUserMail(), username, authenticator.getCurrUID())
    }

    /**
     * Toast on successful logIn
     */
    private fun updateOnSuccess() {
        Toast.makeText(vmAuth.ctx, "Successful login", Toast.LENGTH_SHORT).show()
        startProfileActivity()
    }

    /**
     *  Toast on failed authentication
     *  @param: text to display in toast. Can be changed base on error
     */
    private fun updateOnFail(text: String = "Authentication failed") {
        Toast.makeText(vmAuth.ctx, text, Toast.LENGTH_SHORT).show()
    }

    /**
     *  Reset Password function
     *  @param email email adress for the password reset
     */
    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnFailureListener {
            Toast.makeText(vmAuth.ctx, R.string.authentication_forgotPassword, Toast.LENGTH_SHORT)
                .show()
        }
    }
}