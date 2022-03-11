package ch.sdp.vibester.auth

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

interface Authenticator {

    val auth: FirebaseAuth
    val googleClient: GoogleSignInClient

    fun signIn(email: String, password: String): Task<AuthResult>
    fun createAccount(email: String, password: String): Task<AuthResult>
    fun googleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, email: TextView)
}