package ch.sdp.vibester.helper

import android.content.Context
import android.content.Intent
import ch.sdp.vibester.activity.MainActivity

class IntentSwitcher private constructor() {
    companion object {
        fun switchBackToMain(ctx: Context) {
            ctx.startActivity(Intent(ctx, MainActivity::class.java))
        }
    }
}