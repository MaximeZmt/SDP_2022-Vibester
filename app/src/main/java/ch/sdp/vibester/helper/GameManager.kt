package ch.sdp.vibester.helper

import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient
import java.io.Serializable

/**
 * Game Manager to set up a solo game for a chosen mode.
 */
open class GameManager : Serializable {
    private var score = 0
    open var gameSize = 5
    var numPlayedSongs = 0
    open lateinit var currentSong: Song
    var gameSongList: MutableList<Pair<String, String>> = mutableListOf()
    private val correctSongs = mutableListOf<Song>()
    private var wrongSongs = mutableListOf<Song>()
    var nextSongInd = 0

    /**
     * Set a shuffled songList for a game
     * @param jsonMeta: JSON song list from lastfm API
     * @param method: method for a game
     */
    fun setGameSongList(jsonMeta: String, method: String) {
        gameSongList = SongList(jsonMeta, method).getShuffledSongList()
    }

    /**
     * Increase a score if the game was correct
     */
    fun increaseScore() {
        score++
    }

    /**
     * Get current number of played songs in a game
     */
    fun getPlayedSongsCount(): Int {
        return numPlayedSongs
    }

    /**
     * Get a list of songs that were guessed correct in the game
     */
    fun getCorrectSongs(): MutableList<Song> {
        return correctSongs
    }

    /**
     * Get a list of songs that were guessed wrong in the game
     */
    fun getWrongSongs(): MutableList<Song> {
        return wrongSongs
    }

    /**
     * Get a score for a game (current and total)
     */
    fun getScore(): Int {
        return score
    }

    /**
     * Get a current playing song
     */
    @JvmName("getCurrentSong1")
    fun getCurrentSong(): Song {
        return currentSong
    }

    /**
     * Get a songs list set up for the game
     */
    fun getSongList(): MutableList<Pair<String, String>> {
        return gameSongList
    }

    /**
     * Add a correct song to a correct guessed song list
     */
    fun addCorrectSong() {
        correctSongs.add(currentSong)
    }

    /**
     * Add a wrong song to a wrong guessed song list
     */
    fun addWrongSong() {
        wrongSongs.add(currentSong)
    }

    /**
     * Check whether the number of played songs w.r.t. to game size
     * @return: Boolean. true if the number of played songs is less than game size
     *          false otherwise
     */
    fun checkGameStatus(): Boolean {
        return numPlayedSongs < gameSize
    }


    /**
     * Set the next song to play. It can happened that the song from a list is not present in
     * Itunes API. In such case, the exception is called and the function is rerun with the
     * next song in the list.
     * @return: true if the next song to play is set
     *          false otherwise
     */
    fun setNextSong(): Boolean {
        if (nextSongInd < gameSongList.size) {
            val songPair = gameSongList[nextSongInd]
            val songName = songPair.first + " " + songPair.second
            try {
                currentSong =
                    Song.singleSong(ItunesMusicApi.querySong(songName, OkHttpClient(), 1).get())
                nextSongInd++
                numPlayedSongs++
            } catch (e: Exception) {
                nextSongInd++
                setNextSong()
            }
            return true
        }
        return false
    }
}