package ch.sdp.vibester.helper

import android.content.Context
import android.content.Intent
import ch.sdp.vibester.activity.WelcomeActivity

class IntentSwitcher private constructor() {
    companion object {
        fun switchBackToWelcome(ctx: Context) {
            ctx.startActivity(Intent(ctx, WelcomeActivity::class.java))
        }
    }
}