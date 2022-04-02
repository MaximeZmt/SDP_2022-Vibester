package ch.sdp.vibester.helper

import android.media.MediaPlayer
import ch.sdp.vibester.api.AudioPlayer
import ch.sdp.vibester.api.ItunesMusicApi
import ch.sdp.vibester.model.Song
import okhttp3.OkHttpClient
import java.util.concurrent.CompletableFuture

class TypingGameManager: GameManager() {
    private lateinit var mediaPlayer: CompletableFuture<MediaPlayer>
    private var nextSongInd = 0

    /**
     * Set a media player. Used for testing.
     */
    fun setMediaPlayer(mediaPlayer: CompletableFuture<MediaPlayer>) {
        this.mediaPlayer = mediaPlayer
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

    /**
     * Set the next song to play. It can happened that the song from a list is not present in
     * Itunes API. In such case, the exception is called and the function is rerun with the
     * next song in the list.
     * @return: true if the next song to play is set
     *          false otherwise
     */
    fun setNextSong(): Boolean {
        if (nextSongInd < super.gameSongList.size) {
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