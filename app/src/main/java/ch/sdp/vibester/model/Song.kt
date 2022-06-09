package ch.sdp.vibester.model

import android.util.Log
import org.json.JSONObject
import java.io.Serializable

/**
 * A class representing a song
 * @param jsonO a JSONObject that will be parsed
 */
class Song(jsonO: JSONObject) : Serializable {

    private var previewUrl = ""
    private var artworkUrl = ""
    private var trackName = ""
    private var artistName = ""

    // Here is the parsing from string (JSON) to retrieve the values
    init {
        try {
            previewUrl = jsonO.getString("previewUrl")
            artworkUrl = jsonO.getString("artworkUrl100")
            trackName = jsonO.getString("trackName")
            artistName = jsonO.getString("artistName")
        } catch (e: Exception) {
            throw IllegalArgumentException("Song constructor, bad argument")
        }
    }

    /**
     * Getter that return the previewUrl
     * @return String that points towards an audio stream
     */
    fun getPreviewUrl(): String {
        return previewUrl
    }

    /**
     * Getter that return the artworkUrl
     * @return String that points towards the artwork
     */
    fun getArtworkUrl(): String {
        return artworkUrl
    }

    /**
     * Getter that return the track name
     * @return String containing the track name
     */
    fun getTrackName(): String {
        return trackName
    }

    /**
     * Getter that return the artist name
     * @return String containing the artist name
     */
    fun getArtistName(): String {
        return artistName
    }

    companion object {
        /**
         * Build a song given a json string
         * @param str the json string
         * @return A Song object
         */
        fun singleSong(str: String): Song {
            try {
                val jsonObj = JSONObject(str)
                Log.d("debug","doNotRemove1")
                val jsonArray = jsonObj.getJSONArray("results")
                Log.d("debug","doNotRemove2")
                val jsonRes = jsonArray.getJSONObject(0)
                Log.d("debug","doNotRemove3")
                return Song(jsonRes)
            } catch (e: Exception) {
                throw IllegalArgumentException("Song constructor, bad argument")
            }
        }

        /**
         * Build a song object giving manually argument
         * @param previewUrl the preview url of the song
         * @param artworkUrl the artwork url of the song
         * @param trackName the name of the song
         * @param artistName the name of the singer
         * @return a Song object
         */
        fun songBuilder(previewUrl: String = "", artworkUrl: String = "", trackName: String = "", artistName: String = ""): Song {
            val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"artistName":"$artistName", 
                "trackName":"$trackName",
                "previewUrl":"$previewUrl",
                "artworkUrl100":"$artworkUrl"
                }]
            }
            """

            return Song.singleSong(inputTxt)
        }

        /**
         * Build a song list given a json string
         * @param str the json string
         * @return A Song arraylist object
         */
        fun listSong(str: String): ArrayList<Song> {
            try {
                val myList = ArrayList<Song>()
                val jsonObj = JSONObject(str)
                val countResult = jsonObj.getInt("resultCount")
                val jsonArray = jsonObj.getJSONArray("results")
                for (i in 0 until countResult) {
                    myList.add(Song(jsonArray.getJSONObject(i)))
                }
                return myList
            } catch (e: Exception) {
                throw IllegalArgumentException("Song constructor, bad argument")
            }
        }

    }

}