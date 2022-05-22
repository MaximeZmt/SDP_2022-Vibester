package ch.sdp.vibester.helper

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import java.io.Serializable

class IntentSwitcher private constructor() {
    companion object {
        fun switch(ctx: Context, arg: Class<*>?, extrasMap: Map<String, Serializable>? = null) {
            val intent = Intent(ctx, arg)
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
            extrasMap?.forEach{ entry -> intent.putExtra(entry.key, entry.value) }
            ctx.startActivity(intent)
        }
    }
}