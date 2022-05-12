package ch.sdp.vibester.model

import android.content.Context
import android.os.Environment
import ch.sdp.vibester.api.LastfmMethod
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * Process the fetched data from the external storage of the app.
 * Mainly, it creates a list of songs in the form Pair("$songName", "$artistName")
 * @param ctx: Context of the caller, to fetch the downloaded folder
 * @param method: Chosen playlist type
 */
class OfflineSongList(ctx: Context, method: String) {
    private var songList = mutableListOf<Pair<String, String>>()
    private var page = ""
    private var songsPerPage = ""
    private var totalPages = ""
    private var totalSongs = ""
    private var context = ctx
    private var emptySongs: Boolean = false

    init {
        try {
            var tracksField = "tracks"
            if (method == LastfmMethod.BY_ARTIST.method) {
                tracksField = "toptracks"
            }

            fillList()

            page = "1"
            songsPerPage = "100"
            totalPages = "20"
            totalSongs = "2000"

        } catch (e: Exception) {
            throw IllegalArgumentException("OfflineSongList constructor, failed reading from file or empty file!")
        }
    }

    /**
     * Adds from the downloads to the list of songs ("$songName", "$artistName")
     * Saves the list of songs in songList
     */
    private fun fillList() {
        var records = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "records.txt")


        if(!records.exists() || records.length() == 0L) {
            //There are no downloaded songs, keep the song list empty
            emptySongs = true
        } else {
            var reader = BufferedReader(FileReader(records))
            var currentLine = reader.readLine()

            while(currentLine != null) {
                var trimmed = currentLine.trim()
                if(trimmed.isNotEmpty()) {
                    val split = trimmed.split("-")
                    if(split.size == 2) {
                        songList.add(Pair(split[0], split[1]))
                    }
                }
                currentLine = reader.readLine()
            }
            reader.close()
            emptySongs = false
        }
    }

    /**
     * Getter that return songs for the given tag
     * @return MutableList<Pair<String,String>> of type Pair("$songName", "$artistName")
     */
    fun getSongList(): MutableList<Pair<String, String>> {
        return songList
    }

    /**
     * Getter that return shuffled song list
     * @return MutableList<Pair<String,String>> of type Pair("$songName", "$artistName")
     */
    fun getShuffledSongList(): MutableList<Pair<String, String>> {
        return songList.asSequence().shuffled().toMutableList()
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

    /**
     * Getter that returns whether there are downloaded songs available or not
     * @return Boolean emptySongs
     */
    fun getEmptySongs(): Boolean {
        return emptySongs
    }
}