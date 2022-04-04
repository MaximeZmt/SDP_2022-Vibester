package ch.sdp.vibester

import android.content.Intent
import ch.sdp.vibester.activity.LyricsBelongGameActivity
import ch.sdp.vibester.helper.GameManager

class GenreToLyrics: GenreTemporary() {
    override fun switchToGame(response: String, method: String) {
        super.switchToGame(response, method)
        val gameManager = GameManager()
        gameManager.setGameSongList(response, method)
        val newIntent = Intent(this, LyricsBelongGameActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        startActivity(newIntent)
    }
}