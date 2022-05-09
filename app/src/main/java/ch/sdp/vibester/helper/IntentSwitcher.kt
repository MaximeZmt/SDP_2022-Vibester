package ch.sdp.vibester.helper

import android.content.Context
import android.content.Intent
import ch.sdp.vibester.activity.WelcomeActivity
import java.io.Serializable

class IntentSwitcher private constructor() {
    companion object {
        fun switch(ctx: Context, arg: Class<*>?, extrasMap: Map<String, Serializable>?) {
            val intent = Intent(ctx, arg)
            if (extrasMap != null) {
                extrasMap.forEach{
                        entry -> intent.putExtra(entry.key, entry.value)
                }
            }
            ctx.startActivity(intent)
        }


        fun switchBackToWelcome(ctx: Context) {
            ctx.startActivity(Intent(ctx, WelcomeActivity::class.java))
        }
    }
}