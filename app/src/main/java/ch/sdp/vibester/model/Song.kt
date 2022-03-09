package ch.sdp.vibester.model

import org.json.JSONObject
import java.lang.IllegalArgumentException

class Song(jsonMeta: String) {

    private var previewUrl = ""
    private var artworkUrl = ""
    private var trackName = ""
    private var artistName = ""


    init {
        try {
            val jsonObj = JSONObject(jsonMeta)
            val jsonArray = jsonObj.getJSONArray("results")
            val jsonRes = jsonArray.getJSONObject(0)

            previewUrl = jsonRes.getString("previewUrl")
            artworkUrl = jsonRes.getString("artworkUrl100")
            trackName = jsonRes.getString("trackName")
            artistName = jsonRes.getString("artistName")
        } catch(e: Exception){
            throw IllegalArgumentException("Song constructor, bad argument")
        }
    }

    fun getPreviewUrl():String{
        return previewUrl
    }

    fun getArtworkUrl():String{
        return artworkUrl
    }

    fun getTrackName():String{
        return trackName
    }

    fun getArtistName():String{
        return artistName
    }

}