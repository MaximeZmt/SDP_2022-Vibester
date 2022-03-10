package ch.sdp.vibester.auth

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FireBaseAuthenticator : Authenticator {


    override val auth: FirebaseAuth = Firebase.auth

    override fun signInWithGoogle(googleSignInClient: GoogleSignInClient) {
        TODO("Not yet implemented")
    }

    override fun signIn(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override fun createAccount(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }
}