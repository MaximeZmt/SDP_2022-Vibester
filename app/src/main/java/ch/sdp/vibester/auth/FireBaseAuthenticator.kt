package ch.sdp.vibester.auth

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import ch.sdp.vibester.R
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                    Log.e("IDTOK", idToken!!)
                    val ficred = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(ficred).addOnCompleteListener{ task->
                        if(task.isSuccessful){

                            Log.e("Task", "successful")
                        }else{
                            Log.e("Task","not success")
                        }
                    }




                    return account.email
                } catch (e: ApiException) {
                    Log.e("hey you one", e.stackTraceToString())
                    return "Authentication error"
                }
            } else {
                Log.e("hey you two", "")
                return "Authentication error"
            }
        }

        /**
         * Getter for the current user
         */
        fun getCurrUser(): FirebaseUser? {
            return Firebase.auth.currentUser
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


}