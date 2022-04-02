package ch.sdp.vibester

import android.content.Intent
import ch.sdp.vibester.activity.TypingGameActivity
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.TypingGameManager

class GenreToTyping : GenreTemporary() {

    override fun switchToGame(response: String, method: String) {
        super.switchToGame(response, method)
        val gameManager = TypingGameManager()
        gameManager.setGameSongList(response, method)
        val newIntent = Intent(this, TypingGameActivity::class.java)
        newIntent.putExtra("gameManager", gameManager)
        startActivity(newIntent)
    }
}