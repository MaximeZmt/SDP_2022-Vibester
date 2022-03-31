package ch.sdp.vibester.api

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 * The Callback class when calling different API method
 */
class ApiCallback(val retFuture: CompletableFuture<String>) : Callback {
    override fun onResponse(call: Call, response: Response) {
        retFuture.complete(response.body?.string())
    }

    override fun onFailure(call: Call, e: IOException) {
        retFuture.completeExceptionally(e)
    }
}
