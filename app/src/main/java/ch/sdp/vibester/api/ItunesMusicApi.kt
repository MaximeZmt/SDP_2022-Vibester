package ch.sdp.vibester.api

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import ch.sdp.vibester.model.Song
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException

class ItunesMusicApi private constructor(){

    companion object{
        private var mediaPlayer: MediaPlayer? = null
        private val LOOKUP_URL_BASE = "https://itunes.apple.com/search?limit=1&term="

        fun playFromQuery(query: String, ctx: Context): String {
            var resp: String = ""
            val buildedUrl = LOOKUP_URL_BASE+query.replace(' ', '+')
            val queue = Volley.newRequestQueue(ctx)
            val req = StringRequest(Request.Method.GET, buildedUrl, { response ->
                run {
                    val mySong = Song(response)
                    playAudio(mySong.getPreviewUrl())
                }
            }, {})
            queue.add(req)
            return resp
        }

        private fun playAudio(audioUrl: String){
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
                Log.e("[PlayAudio]", "Error see stacktrace")
                e.printStackTrace()
            }
        }
    }


}