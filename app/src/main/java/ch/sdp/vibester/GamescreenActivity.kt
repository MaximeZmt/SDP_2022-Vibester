package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class GamescreenActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamescreen)

        /* number of players for the game
            currently hardcoded as a placeholder
            will be retrieved from intent launched from the game setup screen
         */
        val players = arrayOf("Kamila", "Jiabao", "Arda", "Laurynas", "Margaux", "Maxime")

        val buzzers = findViewById<LinearLayout>(R.id.buzzersLayout)

        val gamescreenIntent = Intent(this, GamescreenActivity::class.java)

        var i = 1

        val buttons = arrayOfNulls<Button>(players.size)

        for (pName in players) {
            val buttonName = "button" + i.toString()
            val buttonId = resources.getIdentifier(buttonName, "id", packageName)
            val button = findViewById<Button>(buttonId)
            button.text = pName
            buttons.set(i-1, button)
            button.setOnClickListener {
                startActivity(gamescreenIntent)
            }
            buzzers.addView(findViewById<Button>(buttonId))
            i = i + 1
        }

    }
}