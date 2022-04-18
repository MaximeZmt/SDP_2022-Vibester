package ch.sdp.vibester.auth

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

class FireBaseAuthenticator @Inject constructor() {
    companion object{

        /**
         * API: return true if firebase authentication is logged in
         */
        fun isLoggedIn(): Boolean {
            return !(FirebaseAuth.getInstance().currentUser == null)
        }

        /**
         * API: return the mail of the user if logged in otherwise empty string
         */
        fun getCurrentUserMail(): String {
            if (isLoggedIn()) {
                return FirebaseAuth.getInstance().currentUser!!.email.toString()
            } else {
                return ""
            }
        }

    }

    private val auth: FirebaseAuth = Firebase.auth

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
     * @param password passwprd
     * @return Task of the result
     */

    fun createAccount(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun getCurrUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * A function to return the result of google sign in
     * @param requestCode a request code
     * @param resultCode a result code
     * @param data intent returned from google sign in
     */
    fun googleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): String? {
        return if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                account.email
            } catch (e: ApiException) {
                "Authentication error"
            }
        } else {
            "Authentication error"
        }
    }
}