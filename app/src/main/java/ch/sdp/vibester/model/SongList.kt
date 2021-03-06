package ch.sdp.vibester.model

import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.helper.Helper
import org.json.JSONArray
import org.json.JSONObject

/**
 * Process the fetched data from Lastfm query.
 * Mainly, it creates a list of songs in the form Pair("$songName", "$artistName")
 * @param jsonMeta: Lastfm fetched data
 */
class SongList(jsonMeta: String, method: String): SuperSongList() {
    private var page = ""
    private var songsPerPage = ""
    private var totalPages = ""
    private var totalSongs = ""

    init {
        try {
            val jsonObj = JSONObject(jsonMeta)
            var tracksField = "tracks"
            if (method == LastfmMethod.BY_ARTIST.method) {
                tracksField = "toptracks"
            }
            val jsonRes = jsonObj.getJSONObject(tracksField)

            val nonFilteredSongs = jsonRes.getJSONArray("track")
            filterSongs(nonFilteredSongs)

            val attributes = jsonRes.getJSONObject("@attr")
            page = attributes.getString("page")
            songsPerPage = attributes.getString("perPage")
            totalPages = attributes.getString("totalPages")
            totalSongs = attributes.getString("total")

        } catch (e: Exception) {
            throw IllegalArgumentException("SongsList constructor, bad argument")
        }
    }

    /**
     * Converts JSONArray to the list of songs ("$songName", "$artistName")
     * @param nonFilteredSongs: JSONArray of songs from the Lastfm query
     * Saves the list of songs in songList
     */
    private fun filterSongs(nonFilteredSongs: JSONArray) {
        val songsLength = nonFilteredSongs.length()
        var i = 0
        while (i < songsLength) {
            val songObj = nonFilteredSongs.getJSONObject(i)
            val songName = songObj.getString("name").lowercase()
            val artistDetails = songObj.getJSONObject("artist")
            val artistName = artistDetails.getString("name").lowercase()
            songList.add(Pair(songName, artistName))
            ++i
        }
    }


    /**
     * Getter that return page number from the query
     * @return String page
     */
    fun getPage(): String {
        return page
    }

    /**
     * Getter that return total number of songs in the tag
     * @return String totalSongs
     */
    fun getTotalSongs(): String {
        return totalSongs
    }

    /**
     * Getter that return the total number of pages with songs by tag
     * @return String totalPages
     */
    fun getTotalPages(): String {
        return totalPages
    }

    /**
     * Getter that return the total number of songs in the page
     * @return String perPage
     */
    fun getSongsPerPage(): String {
        return songsPerPage
    }

}