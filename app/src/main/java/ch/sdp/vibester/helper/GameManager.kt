package ch.sdp.vibester.helper

import android.media.MediaPlayer
import ch.sdp.vibester.api.*
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * Game Manager to set up a solo game for a chosen mode.
 */
class GameManager: Serializable{
    private var score = 0;
    private var nextSongInd = 0
    private val gameSize = 10
    private var numPlayedSongs = 0
    private lateinit var currentSong: Song
    private var gameSongList: List<String> = mutableListOf()
    private lateinit var mediaPlayer: CompletableFuture<MediaPlayer>
    private val correctSongs = mutableListOf<Song>()
    private var wrongSongs = mutableListOf<Song>()

    /**
     * Set a shuffled songlist for a game
     * @param jsonMeta: JSON song list from lastfm API
     * @param method: method for a game
     */
    fun setGameSongList(jsonMeta:String, method:String){
        gameSongList = SongList(jsonMeta, method).getShuffledSongList();
    }

    /**
     * Increase a score if the game was correct
     */
    fun increaseScore(){
        score++;
    }

    /**
     * Get current number of played songs in a game
     */
    fun getPlayedSongs():Int{
        return numPlayedSongs
    }

    /**
     * Get a list of songs that were guessed correct in the game
     */
    fun getCorrectSongs(): MutableList<Song>{
        return correctSongs
    }

    /**
     * Get a list of songs that were guessed wrong in the game
     */
    fun getWrongSongs(): MutableList<Song>{
        return wrongSongs
    }

    /**
     * Get a score for a game (current and total)
     */
    fun getScore():Int {
        return score
    }

    /**
     * Get a current playing song
     */
    fun getCurrentSong(): Song{
        return currentSong
    }

    /**
     * Get a songs list set up for the game
     */
    fun getSongList():List<String>{
        return gameSongList
    }

    /**
     * Add a correct song to a correct guessed song list
     */
    fun addCorrectSong(){
        correctSongs.add(currentSong)
    }

    /**
     * Add a wrong song to a wrong guessed song list
     */
    fun addWrongSong(){
        wrongSongs.add(currentSong)
    }

    /**
     * Check whether the number of played songs w.r.t. to game size
     * @return: Boolean. true if the number of played songs is less than game size
     *          false otherwise
     */
    fun checkGameStatus():Boolean{
        if(numPlayedSongs < gameSize){
            return true
        }
        return false
    }

    /**
     * Set the next song to play. It can happend that the song from a list is not present in
     * Itunes API. In such case, the exception is called and the function is rerun with the
     * next song in the list.
     * @return: true if the next song to play is set
     *          false otherwise
     */
    fun setNextSong():Boolean {
        if(nextSongInd < gameSongList.size){
            val songName = gameSongList[nextSongInd]
            try{
                currentSong = Song.singleSong(ItunesMusicApi.querySong(songName, OkHttpClient(), 1).get())
                nextSongInd++
                numPlayedSongs++
            }
            catch (e:Exception) {
                nextSongInd++
                setNextSong()
            }
            return true
        }
        return false
    }

    /**
     * Set a media player. Used for testing.
     */
    fun setMediaPlayer(mediaPlayer: CompletableFuture<MediaPlayer>){
        this.mediaPlayer = mediaPlayer
    }

    /**
     * Play current song with media player.
     */
    fun playSong(){
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
    fun stopMediaPlayer(){
        mediaPlayer.get().stop()
    }


}