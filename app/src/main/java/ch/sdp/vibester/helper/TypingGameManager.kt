package ch.sdp.vibester.helper

import android.media.MediaPlayer
import ch.sdp.vibester.api.AudioPlayer
import java.util.concurrent.CompletableFuture

class TypingGameManager: GameManager() {
    private lateinit var mediaPlayer: CompletableFuture<MediaPlayer>

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

}