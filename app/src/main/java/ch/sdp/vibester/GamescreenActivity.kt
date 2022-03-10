package ch.sdp.vibester

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AlertDialogLayout


class GamescreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamescreen)

        /* number of players for the game
            currently hardcoded as a placeholder
            will be retrieved from intent launched from the game setup screen
         */
        val players = arrayOf("Kamila", "Jiabao", "Arda", "Laurynas")

        val gamescreenIntent = Intent(this, GamescreenActivity::class.java)

        // hardcoded test values
        val song = "Demo"
        val artist = "The Placeholders"

        val buildPopup = AlertDialog.Builder(this)
        buildPopup.setTitle("Solution")
        buildPopup.setMessage("The song was " + song + " by " + artist)

        buildPopup.setPositiveButton("Correct") { dialog, which ->
            Toast.makeText(applicationContext,
                "Congrats!", Toast.LENGTH_SHORT).show()
        }

        buildPopup.setNegativeButton("Wrong") { dialog, which ->
            Toast.makeText(applicationContext,
                "Too bad!", Toast.LENGTH_SHORT).show()
        }

        val allPoints = arrayOf(1, 2, 3, 4)

        buildScores(players, allPoints)
        buildBuzzers(players, buildPopup)

    }

    /*
    Programmatically builds the table of scores according to the number of players
     */
    private fun buildScores(players: Array<String>, allPoints: Array<Int>) {

        val scores = findViewById<LinearLayout>(R.id.scoresTable)
        val viewsOfPoints = arrayOfNulls<TableRow>(players.size)

        var i = 0

        for (pName in players) {

            val score = TableRow(this)
            score.gravity = Gravity.CENTER

            val nameView = TextView(this)
            nameView.text = pName
            nameView.height = 75
            nameView.width = 300
            nameView.gravity = Gravity.LEFT

            val points = TextView(this)
            points.text = allPoints[i].toString()
            points.height = 75
            points.width = 150
            points.gravity = Gravity.RIGHT

            score.addView(nameView)
            score.addView(points)
            viewsOfPoints.set(i, score)
            scores.addView(score)

            i = i + 1
        }
    }

    /*
    Programmatically builds the buzzers according to the number and names of players.
     */
    private fun buildBuzzers(players: Array<String>, popup: AlertDialog.Builder) {

        val buzzers = findViewById<LinearLayout>(R.id.buzzersLayout)
        val buttons = arrayOfNulls<Button>(players.size)

        var i = 0

        for (pName in players) {

            val button = Button(this)
            button.text = pName
            button.width = 0
            button.height = 150
            buttons.set(i, button)
            button.setOnClickListener {
                popup.show()
            }
            buzzers.addView(button)

            i = i + 1
        }
    }
}