package ch.sdp.vibester.model

import org.json.JSONArray
import org.json.JSONObject
import java.lang.IllegalArgumentException

class SongsList(jsonMeta: String) {

    private var songs = mutableListOf<String>()
    private var page = ""
    private var songsPerPage = ""
    private var totalPages = ""
    private var totalSongs = ""

    init {
        try {
            val jsonObj = JSONObject(jsonMeta)
            var jsonRes = jsonObj.getJSONObject("tracks")
            var nonFilteredSongs = jsonRes.getJSONArray("track")
            songs = filterSongs(nonFilteredSongs)

            var attributes = jsonRes.getJSONObject("@attr")
            page = attributes.getString("page")
            songsPerPage = attributes.getString("perPage")
            totalPages = attributes.getString("totalPages")
            totalSongs = attributes.getString("total")

        } catch(e: Exception){
            throw IllegalArgumentException("SongsList constructor, bad argument")
        }
    }

     fun filterSongs(nonFilteredSongs: JSONArray): MutableList<String> {
        val songsLength = nonFilteredSongs.length()
        var songList = mutableListOf<String>()
        var  i = 0
        while(i < songsLength) {
            var songObj = nonFilteredSongs.getJSONObject(i)
            val songName = songObj.getString("name")
            val artistDetails = songObj.getJSONObject("artist")
            val artistName = artistDetails.getString("name")
            songList.add("$songName $artistName")
            ++i
        }
        return songList
    }

    /**
     * Getter that return songs for the given tag
     * @return MutableList<String> of type "$artistName $songName"
     */
    fun getSongs():MutableList<String>{
        return songs
    }

    /**
     * Getter that return page number from the query
     * @return String page
     */
    fun getPage():String{
        return page
    }

    /**
     * Getter that return total number of songs in the tag
     * @return String totalSongs
     */
    fun getTotalSongs():String{
        return totalSongs
    }

    /**
     * Getter that return the total number of pages with songs by tag
     * @return String totalPages
     */
    fun getTotalPages():String{
        return totalPages
    }

    /**
     * Getter that return the total number of songs in the page
     * @return String perPage
     */
    fun getSongsPerPage():String{
        return songsPerPage
    }

}