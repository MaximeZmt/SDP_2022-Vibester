package ch.sdp.vibester.model

import android.util.Log
import org.json.JSONObject
import java.lang.IllegalArgumentException

/**
 * A class representing a song
 * @param jsonO a JSONObject that will be parsed
 */
class Song(jsonO: JSONObject) {

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
        } catch(e: Exception){
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

    companion object{
        fun singleSong(str: String): Song{
            try {
                val jsonObj = JSONObject(str)
                val jsonArray = jsonObj.getJSONArray("results")
                val jsonRes = jsonArray.getJSONObject(0)
                return Song(jsonRes)
            }catch(e: Exception){
                throw IllegalArgumentException("Song constructor, bad argument")
            }
        }

        fun listSong(str: String): ArrayList<Song>{
            try{
                Log.e("prout","-1")
                val myList = ArrayList<Song>()
                Log.e("prout","0")
                val jsonObj = JSONObject(str)
                Log.e("prout","1")
                val countResult = jsonObj.getInt("resultCount")
                Log.e("prout","2")
                val jsonArray = jsonObj.getJSONArray("results")
                Log.e("prout","cacs")
                for (i in 0..(countResult-1)){
                    Log.e("prout", i.toString())
                    myList.add(Song(jsonArray.getJSONObject(i)))
                    Log.e("prout", "done")
                }
                return myList
            }catch(e: Exception){
                throw IllegalArgumentException("Song constructor, bad argument")
            }
        }

    }

}