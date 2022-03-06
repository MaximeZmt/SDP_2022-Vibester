package ch.sdp.vibester.api

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.IOException

class ItunesMusicApi {
    var mediaPlayer: MediaPlayer? = null
    val LOOKUP_URL_BASE = "https://itunes.apple.com/search?limit=1&term="
    val JSON_API_LOOKUP_ARRAY = "results"
    val JSON_API_LOOKUP_PREV = "previewUrl"

    fun findUrlPreview(query: String, ctx: Context): String {
        var resp: String = ""
        val buildedUrl = LOOKUP_URL_BASE+query.replace(' ', '+')
        val queue = Volley.newRequestQueue(ctx)
        val req = StringRequest(Request.Method.GET, buildedUrl, Response.Listener<String>{ response ->
            run {
                Log.e("Found", parseJsonFindPreviewUrl(response))
                playAudio(parseJsonFindPreviewUrl(response))
            }
        }, Response.ErrorListener {})
        queue.add(req)
        return resp
    }

    fun parseJsonFindPreviewUrl(response: String): String{
        val jsonObj = JSONObject(response)
        val jsonArray = jsonObj.getJSONArray(JSON_API_LOOKUP_ARRAY)
        val jsonRes = jsonArray.getJSONObject(0)
        return jsonRes.getString(JSON_API_LOOKUP_PREV)
    }

    fun playAudio(audioUrl: String){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        try {
            mediaPlayer!!.setDataSource(audioUrl)
            mediaPlayer!!.prepare()

            mediaPlayer!!.setOnPreparedListener(MediaPlayer.OnPreparedListener {
                mediaPlayer!!.start()
            })

        }catch (e: IOException){
            e.printStackTrace()
        }
    }


}