package ch.sdp.vibester.model

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * Process the fetched data from the external storage of the app.
 * Mainly, it creates a list of songs in the form Pair("$songName", "$artistName")
 */
class OfflineSongList(externalDir: File): SuperSongList() {
    private val page = "1"
    private val songsPerPage = "100"
    private val totalPages = "20"
    private val totalSongs = "2000"
    private var externals = externalDir
    private var emptySongs: Boolean = false

    init {
        fillList()
    }

    /**
     * Adds from the downloads to the list of songs ("$songName", "$artistName")
     * Saves the list of songs in songList
     */
    private fun fillList() {
        val records = File(externals, "records.txt")

        if (!records.exists() || records.length() == 0L) {
            //There are no downloaded songs, keep the song list empty
            emptySongs = true
        } else {
            val reader = BufferedReader(FileReader(records))
            var currentLine = reader.readLine()

            while (currentLine != null) {
                val trimmed = currentLine.trim()
                if (trimmed.isNotEmpty()) {
                    val split = trimmed.split("-")
                    if (split.size == 2) {
                        songList.add(Pair(split[0].trim(), split[1].trim()))
                    }
                }
                currentLine = reader.readLine()
            }
            reader.close()
            emptySongs = false
        }
    }

    /**
     * Getter that return shuffled song list
     * @return MutableList<Pair<String,String>> of type Pair("$songName", "$artistName")
     */
    fun getShuffledDownloadedSongList(): MutableList<Pair<String, String>> {
        return super.getShuffledSongList()
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