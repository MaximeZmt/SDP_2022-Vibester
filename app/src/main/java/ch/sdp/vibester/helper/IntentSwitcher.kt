package ch.sdp.vibester.helper

import android.content.Context
import android.content.Intent
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
    }
}