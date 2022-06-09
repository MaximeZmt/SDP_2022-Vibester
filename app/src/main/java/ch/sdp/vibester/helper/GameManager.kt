package ch.sdp.vibester.helper

import android.media.MediaPlayer
import android.util.Log
import ch.sdp.vibester.api.AudioPlayer
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.OfflineSongList
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Game Manager to set up a local game for a chosen mode.
 */
open class GameManager : Serializable {
    var gameSize = 5
    var numPlayedSongs = 0
    var gameMode = ""
    var difficultyLevel = 0

    open lateinit var currentSong: Song
    var gameSongList: MutableList<Pair<String, String>> = mutableListOf()
    private var correctSongs = mutableListOf<Song>()
    private var wrongSongs = mutableListOf<Song>()
    private var artistName = ""
    private var songName = ""
    var nextSongInd = 0
    private lateinit var mediaPlayer: CompletableFuture<MediaPlayer>

    private var hasInternet: Boolean = true
    private lateinit var externals: File

    /**
     * Set a shuffled songList for a game
     * HAS OFFLINE
     * @param jsonMeta: JSON song list from lastfm API
     * @param method: method for a game
     */
    fun setGameSongList(jsonMeta: String, method: String) {
        if (hasInternet) {
            gameSongList = SongList(jsonMeta, method).getShuffledSongList()
        } else {
            gameSongList = OfflineSongList(externals).getShuffledDownloadedSongList()
            Log.d("Size of the list", "${gameSongList.size}")
            Log.d("First one is =====================================================", gameSongList[0].toString())
        }
    }

    /**
     * Sets the offline fields of this class. To be passed to OfflineSongList for access to storage.
     * @param external: getExternalFilesDir(ENVIRONMENT.DIRECTORY_DOWNLOADS) call's result
     * @param internet: True for available
     */
    fun setOffline(external: File, internet: Boolean) {
        externals = external
        hasInternet = internet
    }

    /**
     * Retrieves the internet connection availability. Used to check if we can use Glide in BuzzerScreen or not.
     */
    fun getInternet(): Boolean {
        return hasInternet
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
        return correctSongs.size*difficultyLevel
    }

    /**
     * Get a current playing song
     */
    @JvmName("getCurrentSong1")
    fun getCurrentSong(): Song {
        return if(this::currentSong.isInitialized) {
            currentSong
        } else {
            Song.songBuilder("http://example.com", "http://example.com", songName, artistName)
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
        correctSongs.add(getCurrentSong())
    }

    /**
     * Add a wrong song to a wrong guessed song list
     */
    fun addWrongSong() {
        wrongSongs.add(getCurrentSong())
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
     * HAS OFFLINE
     * @return: true if the next song to play is set
     *          false otherwise
     */
    fun setNextSong(): Boolean {
        if (nextSongInd < gameSongList.size) {
            val songPair = gameSongList[nextSongInd]
            songName = songPair.first
            artistName = songPair.second
            if(hasInternet) {
                val songName = songPair.first + " " + songPair.second
                try {
                    currentSong =
                        Song.singleSong(ItunesMusicApi.querySong(songName, OkHttpClient(), 1).get())
                    Log.d("I'M ONLINEEEEEEEEEEEEEEEEEEEEEEEEE", currentSong.getTrackName())
                    nextSongInd++
                    numPlayedSongs++
                } catch (e: Exception) {
                    nextSongInd++
                    return setNextSong()
                }
                return true
            } else {
                return setNextOfflineSong(songPair)
            }
        }
        return false
    }

    /**
     * Function that handles setting the next song in the offline mode. Modularized to avoid
     * Code Climate issues.
     * HAS OFFLINE
     * @param songPair: The pair of song-artist that corresponds to the next track.
     */
    private fun setNextOfflineSong(songPair: Pair<String, String>): Boolean {
        try {
            val properties = File(externals, "properties.txt")
            if(!properties.exists() || properties.length() == 0L) {
                throw Exception("No songs available in properties.txt")
            } else {
                readFromFile(properties, songPair)
            }
        } catch (e: Exception) {
            Log.d("GameManager/setNextSong/Offline", e.message.toString())
            return false
        }
        return true
    }

    /**
     * Function that reads from the given file to check if the given pair of strings exist within it.
     * If yes, then sets the current song.
     * HAS OFFLINE
     * @param file: The file in which to check for the existence of the pair
     * @param songPair: The pair corresponding to the next song-artist.
     */
    private fun readFromFile(file: File, songPair: Pair<String, String>) {
        val reader = BufferedReader(FileReader(file))
        var currentLine = reader.readLine()

        while (currentLine != null) {
            val trimmed = currentLine.trim()
            if (trimmed.isNotEmpty()) {
                val split = trimmed.split(" - ")
                if (split.size == 4) {
                    if (split[0].trim().lowercase() == songPair.first.lowercase()
                    &&  split[1].trim().lowercase() == songPair.second.lowercase()) {
                        currentSong = Song.songBuilder(split[3].trim(), split[2].trim(), split[0].trim(), split[1].trim())
                        nextSongInd++
                        numPlayedSongs++
                        break
                    }
                } else {
                    throw Exception("Inconsistent number of information in properties.txt")
                }
            } else {
                throw Exception("Trimmed line is empty")
            }
            currentLine = reader.readLine()
        }
        reader.close()
    }

    /**
     * Check if mediaPlayer is initialized
     */
    fun initializeMediaPlayer(): Boolean {
        return this::mediaPlayer.isInitialized
    }

    /**
     * Play current song with media player.
     * HAS OFFLINE
     */
    fun playSong() {
        if(hasInternet) {
            mediaPlayer = AudioPlayer.playAudio(currentSong.getPreviewUrl())
        } else {
            val trackName = currentSong.getTrackName().trim() + " - " + currentSong.getArtistName().trim()
            val media = File(externals, "extract_of_$trackName")
            if(media.exists()) {
                mediaPlayer = AudioPlayer.playAudio(media.absolutePath)
            }

        }
    }

    /**
     * Check if media player is playing
     * @return: true if media player is playing
     *          false otherwise
     */
    fun playingMediaPlayer(): Boolean {
        if (initializeMediaPlayer()) {
            return mediaPlayer.get().isPlaying
        }
        return false
    }

    /**
     * Stop media player
     */
    fun stopMediaPlayer() {
        mediaPlayer.get().stop()
    }
}