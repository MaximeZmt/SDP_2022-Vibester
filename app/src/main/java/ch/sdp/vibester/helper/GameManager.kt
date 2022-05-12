package ch.sdp.vibester.helper

import android.media.MediaPlayer
import ch.sdp.vibester.api.AudioPlayer
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Game Manager to set up a local game for a chosen mode.
 */
open class GameManager : Serializable {
    private var score = 0 // we can delete this (in a next refactor) and use the size of the correct song list instead
    var gameSize = 5
    var numPlayedSongs = 0
    var gameMode = ""
    open lateinit var currentSong: Song
    var gameSongList: MutableList<Pair<String, String>> = mutableListOf()
    private val correctSongs = mutableListOf<Song>() //TODO: question: why this is a val but the next one is a var?
    private var wrongSongs = mutableListOf<Song>()
    private var artistName = ""
    private var songName = ""
    var nextSongInd = 0

    private lateinit var mediaPlayer: CompletableFuture<MediaPlayer>

    /**
     * set the number of songs in this game
     * @param numberOfSongs: number of songs
     */
    @JvmName("setGameSize1")
    fun setGameSize(numberOfSongs: Int) {
        gameSize = numberOfSongs
    }

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
        if(this::currentSong.isInitialized) {
            return currentSong
        } else {
            return Song.songBuilder("http://example.com", "http://example.com", songName, artistName)
        }
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
            songName = songPair.first
            artistName = songPair.second
            val songName = songPair.first + " " + songPair.second
            try {
                currentSong =
                    Song.singleSong(ItunesMusicApi.querySong(songName, OkHttpClient(), 1).get())
                nextSongInd++
                numPlayedSongs++
            } catch (e: Exception) {
                nextSongInd++
                return setNextSong()
            }
            return true
        }
        return false
    }

    /**
     * Check if mediaPlayer is initialized
     */
    fun initializeMediaPlayer(): Boolean {
        return this::mediaPlayer.isInitialized
    }

    /**
     * Play current song with media player.
     */
    fun playSong() {
        mediaPlayer = AudioPlayer.playAudio(currentSong.getPreviewUrl())
    }

    /**
     * Check if media player is playing
     * @return: true if media player is playing
     *          false otherwise
     */
    fun playingMediaPlayer(): Boolean {
        return mediaPlayer.get().isPlaying
    }

    /**
     * Stop media player
     */
    fun stopMediaPlayer() {
        mediaPlayer.get().stop()
    }
}