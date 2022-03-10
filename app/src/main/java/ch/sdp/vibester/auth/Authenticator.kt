package ch.sdp.vibester.auth

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

interface Authenticator {

    val auth: FirebaseAuth

    fun signInWithGoogle(googleSignInClient: GoogleSignInClient)
    fun signIn(email: String, password: String): Task<AuthResult>
    fun createAccount(email: String, password: String): Task<AuthResult>
}