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
import java.lang.IllegalArgumentException

class ItunesMusicApi private constructor(){

    companion object{
        private val LOOKUP_URL_BASE = "https://itunes.apple.com/search?limit=1&term="

        /**
         * Given a String query it will provide the music url preview
         */
        fun playFromQuery(query: String, ctx: Context): String {
            var resp: String = ""
            val buildedUrl = LOOKUP_URL_BASE+query.replace(' ', '+')
            val queue = Volley.newRequestQueue(ctx)
            val req = StringRequest(Request.Method.GET, buildedUrl, { response ->
                run {
                    val mySong = Song(response)
                    var mediaPlayer = playAudio(mySong.getPreviewUrl())
                    mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
                        mediaPlayer.start()
                    })
                }
            }, {})
            queue.add(req)
            return resp
        }

        fun playAudio(audioUrl: String): MediaPlayer{
            var mediaPlayer: MediaPlayer = MediaPlayer()
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            try {
                mediaPlayer.setDataSource(audioUrl)
                mediaPlayer.prepare()


            }catch (e: IOException){
                Log.e("[PlayAudio]", "Error see stacktrace")
                throw IllegalArgumentException("ItunesMusicApi.playAudio bad Url")
            }
            return mediaPlayer
        }
    }


}