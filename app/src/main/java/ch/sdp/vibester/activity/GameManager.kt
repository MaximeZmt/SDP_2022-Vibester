package ch.sdp.vibester.games

import android.media.MediaPlayer
import ch.sdp.vibester.api.*
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient
import java.io.Serializable
import java.util.concurrent.CompletableFuture

class GameManager: Serializable{
    private var score = 0;
    private var nextSongInd = 0
    private val GAME_SIZE = 10
    private var playedSongs = 0
    private lateinit var currentSong: Song
    private var gameSongList: List<String> = mutableListOf()
    private lateinit var mediaPlayer: CompletableFuture<MediaPlayer>
    private val correctSongs = mutableListOf<Song>()
    private var wrongSongs = mutableListOf<Song>()

    fun setGameSongList(jsonMeta:String, method:String){
        gameSongList = SongList(jsonMeta, method).getShuffledSongList();
    }

    fun increaseScore(){
        score++;
    }

    fun getPlayedSongs():Int{
        return playedSongs
    }

    fun getCorrectSongs(): MutableList<Song>{
        return correctSongs
    }

    fun getWrongSongs(): MutableList<Song>{
        return wrongSongs
    }

    fun getScore():Int {
        return score
    }

    fun getCurrentSong(): Song{
        return currentSong
    }

    fun getSongList():List<String>{
        return gameSongList
    }

    fun addCorrectSong(){
        correctSongs.add(currentSong)
    }

    fun addWrongSong(){
        wrongSongs.add(currentSong)
    }

    fun checkNextSong():Boolean{
        if(playedSongs < GAME_SIZE){
            return true
        }
        return false
    }

    fun setNextSong(): Song {
        val songName = gameSongList[nextSongInd]
        try{
            currentSong = Song.singleSong(ItunesMusicApi.querySong(songName, OkHttpClient(), 1).get())
            nextSongInd++
            playedSongs++
        }
        catch (e:Exception) {
            nextSongInd++;
            setNextSong()
        }
        return currentSong
    }

    fun playSong(){
        mediaPlayer = AudioPlayer.playAudio(currentSong.getPreviewUrl())
    }

    fun getMediaPlayer(): CompletableFuture<MediaPlayer> {
        return mediaPlayer
    }

    fun playingMediaPlayer(): Boolean {
        return mediaPlayer.get().isPlaying
    }

    fun stopMediaPlayer(){
        mediaPlayer.get().stop()
    }


}