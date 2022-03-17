package ch.sdp.vibester

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AlertDialogLayout
import androidx.core.view.get


class GamescreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamescreen)

        /* number of players for the game
            currently hardcoded as a placeholder
            will be retrieved from intent launched from the game setup screen
         */
        val players = arrayOf("Kamila", "Jiabao", "Arda", "Laurynas")

        val answer = findViewById<LinearLayout>(R.id.answer)
        val answerText = findViewById<TextView>(R.id.answerText)

        // hardcoded test values
        val song = "Demo"
        val artist = "The Placeholders"

        answerText.text= "The song was $song by $artist"

        val allPoints = arrayOf(1, 2, 3, 4)

        buildScores(players, allPoints)
        buildBuzzers(players, answer)
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

    /*
    Programmatically builds the buzzers according to the number and names of players.
     */
    private fun buildBuzzers(players: Array<String>, answer: LinearLayout) {

        val buzzers = findViewById<LinearLayout>(R.id.buzzersLayout)
        val buttons = arrayOfNulls<Button>(players.size)

        var i = 0

        for (pName in players) {

            val button = Button(this)
            button.id = i
            button.text = pName
            button.width = 100
            button.height = 0
            buttons.set(i, button)
            button.setOnClickListener {
                answer.visibility = android.view.View.VISIBLE
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

    fun switchToEnding(view: View) {
        val intent = Intent(this, GameEndingScreen::class.java)
        //MOCK VALUES FOR INCORRECT SONGS, ADAPT FROM GAME DATA IN THE FUTURE
        val incArray: Array<String> = arrayOf("One", "Two", "Three")
        val statNames: Array<String> = arrayOf("Hello there",
            "Second Stat",
            "Third Stat",
            "Fourth Stat",
            "Fifth Stat")
        val statRes: Array<String> = arrayOf("General Kenobi",
            "----- *2 -----",
            "----- *3 -----",
            "----- *4 -----",
            "----- *5 -----")

        intent.putExtra("playerName", "Arda")
        intent.putExtra("nbIncorrectSong", 3)
        /*
        intent.putExtra("incorrect_songs", incArray)
        intent.putExtra("names", statNames)
        intent.putExtra("values", statRes)
        */

        //Brute-force singular intents, unable to solve the issue with array intents.
        intent.putExtra("incorrect_song_1", "One")
        intent.putExtra("incorrect_song_2", "Two")
        intent.putExtra("incorrect_song_3", "Three")

        intent.putExtra("stat_1", "Hello there")
        intent.putExtra("stat_res_1", "General Kenobi")
        startActivity(intent)
    }
    
}