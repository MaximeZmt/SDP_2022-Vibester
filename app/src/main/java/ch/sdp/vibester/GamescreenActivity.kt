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


        // receive intent and create array of certain size
        val getIntent = intent.extras

        // get the four names

        val nPlayers = when(getIntent?.getString("Number of players")) {
            "One" -> 1
            "Two" -> 2
            "Three" -> 3
            "Four" -> 4
            else -> 1 // default value
        }

        val answer = findViewById<LinearLayout>(R.id.answer)
        val answerText = findViewById<TextView>(R.id.answerText)

        // hardcoded test values
        val song = "Demo"
        val artist = "The Placeholders"

        answerText.text= "The song was $song by $artist"

        val allPoints = Array<Int>(nPlayers, { i -> 0 })

        val playersFull = arrayOf("Kamila", "Jiabao", "Arda", "Laurynas")
        val players = playersFull.copyOfRange(0, nPlayers)

        val buzIds = fetchBuzIdArray(players.size)

        val updater = BuzzerScoreUpdater(allPoints, buzIds)

        buildScores(players, allPoints)
        buildBuzzers(players, buzIds, answer, updater)
        setAnswerButton(answer, findViewById(R.id.buttonCorrect))
        setAnswerButton(answer, findViewById(R.id.buttonWrong))

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

    private fun fetchBuzIdArray(size: Int): Array<Int> {
        var array = arrayOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3, R.id.buzzer_4, R.id.buzzer_5) // replace magic number here!
        return array.copyOfRange(0, size) // is "size" index included or not
    }

    /*
    Programmatically builds the buzzers according to the number and names of players.
     */
    private fun buildBuzzers(players: Array<String>, buzIds: Array<Int>, answer: LinearLayout, updater: BuzzerScoreUpdater) {

        val buzzers = findViewById<LinearLayout>(R.id.buzzersLayout)
        val buttons = arrayOfNulls<Button>(players.size)

        var i = 0

        for (pName in players) {

            val button = Button(this)
            button.id = buzIds[i]
            button.text = pName
            button.width = 100
            button.height = 150
            buttons.set(i, button)
            button.setOnClickListener {
                answer.visibility = android.view.View.VISIBLE
                findViewById<Button>(R.id.buttonCorrect).setOnClickListener {
                    updater.updateScoresArray(button.id)
                }
            }
            buzzers.addView(button)

            i = i + 1
        }
    }

    private fun setAnswerButton(answer: LinearLayout, button: Button) {
        button.setOnClickListener {
            answer.visibility = android.view.View.INVISIBLE
        }
    }
}