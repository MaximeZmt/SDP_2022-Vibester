package ch.sdp.vibester.model

import org.json.JSONObject
import java.lang.IllegalArgumentException

/**
 * A class representing a song
 * @param jsonMeta a String that will be parsed
 */
class Song(jsonMeta: String) {

    private var previewUrl = ""
    private var artworkUrl = ""
    private var trackName = ""
    private var artistName = ""

    // Here is the parsing from string (JSON) to retrieve the values
    init {
        try {
            val jsonObj = JSONObject(jsonMeta)
            val jsonArray = jsonObj.getJSONArray("results")
            val jsonRes = jsonArray.getJSONObject(0)

            previewUrl = jsonRes.getString("previewUrl")
            artworkUrl = jsonRes.getString("artworkUrl100")
            trackName = jsonRes.getString("trackName")
            artistName = jsonRes.getString("artistName")
        } catch(e: Exception) {
            throw IllegalArgumentException("Song constructor, bad argument")
        }
    }

    /**
     * Getter that return the previewUrl
     * @return String that points towards an audio stream
     */
    fun getPreviewUrl():String{
        return previewUrl
    }

    /**
     * Getter that return the artworkUrl
     * @return String that points towards the artwork
     */
    fun getArtworkUrl():String{
        return artworkUrl
    }

    /**
     * Getter that return the track name
     * @return String containing the track name
     */
    fun getTrackName():String{
        return trackName
    }

    /**
     * Getter that return the artist name
     * @return String containing the artist name
     */
    fun getArtistName():String{
        return artistName
    }

}