package ch.sdp.vibester.auth


import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FireBaseAuthenticator @Inject constructor() {

    private val auth: FirebaseAuth = Firebase.auth


    /**
     * Getter for the current user
     */
    fun getCurrUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    /**
     * Getter for the current user ID
     */
    fun getCurrUID(): String {
        var uid = ""
        if (isLoggedIn()) {
            uid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        return uid
    }


    /**
     * API: return true if firebase authentication is logged in
     */
    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }


    /**
     * API: return the mail of the user if logged in otherwise empty string
     */
    fun getCurrUserMail(): String {
        var mail = ""
        if (isLoggedIn()) {
            mail = FirebaseAuth.getInstance().currentUser!!.email.toString()
        }
        return mail
    }

    /**
     * Getter for the current user ID
     */
    fun getCurrUID(): String {
        var uid = ""
        if (isLoggedIn()) {
            uid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        return uid
    }


    /**
     * A function to log in with email and password
     * @param email email
     * @param password password
     * @return Task of the result
     */
    fun signIn(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    /**
     * A function to create an account with email and password
     * @param email email
     * @param password password
     * @return Task of the result
     */
    fun createAccount(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }


}