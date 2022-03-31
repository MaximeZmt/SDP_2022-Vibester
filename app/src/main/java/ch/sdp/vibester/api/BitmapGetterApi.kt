package ch.sdp.vibester.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors


class BitmapGetterApi private constructor() {
    companion object {

        /**
         * A function to download images from internet and to convert them asynchronously to bitmap
         */
        fun download(url: String): CompletableFuture<Bitmap> {
            val futureBitmap = CompletableFuture<Bitmap>()
            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                throw IllegalArgumentException("Should start with https:// or http://")
            }
            val request: Request = Request.Builder().url(url).build()
            val client = OkHttpClient()
            val executor = Executors.newCachedThreadPool()
            executor.execute {
                try {
                    val response: Response = client.newCall(request).execute()
                    futureBitmap.complete(BitmapFactory.decodeStream(response.body?.byteStream()))
                } catch (e: IOException) {
                    futureBitmap.completeExceptionally(e)
                }
            }
            return futureBitmap
        }

    }
}