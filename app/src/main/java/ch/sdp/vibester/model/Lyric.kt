package ch.sdp.vibester.model

import org.json.JSONObject
import java.lang.Exception
import java.lang.IllegalArgumentException

class Lyric(jsonMeta: String) {

    private var lyricBody = ""

    init {
        try {
            val jsonObject = JSONObject(jsonMeta)
            val jsonArrayMessage = jsonObject.getJSONArray("message")
            val jsonArrayBody = jsonArrayMessage.getJSONArray(1)
            val jsonLyric = jsonArrayBody.getJSONObject(0)

            lyricBody = jsonLyric.getString("lyrics_body")
        } catch (e: Exception) {
            throw IllegalArgumentException("Lyric constructor, bad argument")
        }
    }

    fun getLyricBody():String {
        return lyricBody
    }
}