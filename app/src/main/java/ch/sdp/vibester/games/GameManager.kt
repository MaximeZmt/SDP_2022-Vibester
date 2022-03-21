package ch.sdp.vibester.games

import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.api.LastfmHelper
import ch.sdp.vibester.model.Song
import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient
import java.io.Serializable

class GameManager constructor(method: String, tag: String): Serializable{
    private var score = 0;
    private var currentSong = 0;
    private val gameSize = 10;
    private var gameSongList: List<String> = mutableListOf<String>()

    init {
        gameSongList = LastfmHelper.getRandomSongList(method, tag);
    }

    fun increaseScore(){
        score++;
    }

    fun getScore():Int {
        return score
    }

    fun getSongList():List<String>{
        return gameSongList
    }

    fun nextSong(): Song{
        if(currentSong < gameSongList.size){
            val songName = gameSongList.get(currentSong)
            val songDetails = Song.singleSong(ItunesMusicApi.querySong(songName, OkHttpClient(), 1).get())
            currentSong++;
            return songDetails

        }

        else{
            finish()
        }
        return songDetails
    }

    fun finish(){
    }

}