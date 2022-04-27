package ch.sdp.vibester.auth


import android.content.Context
import android.content.Intent
import ch.sdp.vibester.TestMode
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
            if (isLoggedIn() || TestMode.isTest()) {
                return FirebaseAuth.getInstance().currentUser!!.email.toString()
            } else {
                return ""
            }
        }

        fun getCurrentUID(): String {
            if (isLoggedIn() || TestMode.isTest()) {
                return FirebaseAuth.getInstance().currentUser!!.uid
            } else {
                return ""
            }
        }


        /**
         * A function to return the result of google sign in
         * @param requestCode a request code
         * @param resultCode a result code
         * @param data intent returned from google sign in
         */
        fun googleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, ctx: Context): String? {
            if (requestCode == 1000) {
                try {
                    val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                    val client = Identity.getSignInClient(ctx)
                    val idToken = client.getSignInCredentialFromIntent(data).googleIdToken
                    val ficred = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(ficred)
                    return account.email
                } catch (e: Exception) {
                    return "Authentication error"
                }
            } else {
                return "Authentication error"
            }
        }

    }

    private val auth: FirebaseAuth = Firebase.auth


    /**
     * Getter for the current user
     */
    fun getCurrUser(): FirebaseUser? {
        return Firebase.auth.currentUser
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
     * @param password passwprd
     * @return Task of the result
     */
    fun createAccount(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }


}