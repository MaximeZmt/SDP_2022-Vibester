package ch.sdp.vibester.auth

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FireBaseAuthenticator() {


    val auth: FirebaseAuth = Firebase.auth


    fun signIn(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun createAccount(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

//    @FixMe
//      Commenting this for now until we find a proper way to test it, then will merge it to main
    fun googleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): String? {
        return if(requestCode == 1000) {
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