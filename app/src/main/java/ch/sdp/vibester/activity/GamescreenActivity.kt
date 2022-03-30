package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.sdp.vibester.R


class GamescreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_gamescreen)

        val getIntent = intent.extras
        val nPlayers = getIntent?.getInt("Number of players")

        val answer = findViewById<LinearLayout>(R.id.answer)
        val answerText = findViewById<TextView>(R.id.answerText)
        answerText.text= "The song was Demo by The Placeholders"

        val allPoints = nPlayers?.let { Array<Int>(it, { i -> 0 }) }

        val playersFull = getIntent?.getStringArray("Player Names")
        val players = nPlayers?.let { playersFull?.copyOfRange(0, it) }

        val buzIds = players?.let { fetchBuzIdArray(it.size) }

        if (players != null && allPoints != null) { buildScores(players, allPoints) }
        if (players != null && buzIds != null) { buildBuzzers(players, buzIds, answer) }
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
    private fun buildBuzzers(players: Array<String>, buzIds: Array<Int>, answer: LinearLayout) {

        val buzzers = findViewById<LinearLayout>(R.id.buzzersLayout)
        val buttons = arrayOfNulls<Button>(players.size)

        var i = 0

        for (pName in players) {

            val button = Button(this)
            button.id = buzIds[i]
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
        val intent = Intent(this, GameEndingActivity::class.java)
        //MOCK VALUES FOR INCORRECT SONGS, ADAPT FROM GAME DATA IN THE FUTURE
        val incArray: ArrayList<String> = arrayListOf()
        incArray.addAll(arrayOf("One", "Two", "Three"))

        val statNames: ArrayList<String> = arrayListOf()
        statNames.addAll(arrayOf("Hello there",
            "Second Stat",
            "Third Stat",
            "Fourth Stat",
            "Fifth Stat"))

        val statVal: ArrayList<String> = arrayListOf()
        statVal.addAll(arrayOf("General Kenobi",
            "----- *2 -----",
            "----- *3 -----",
            "----- *4 -----",
            "----- *5 -----"))

        intent.putExtra("playerName", "Arda")
        intent.putExtra("nbIncorrectSong", 3)

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statVal)

        startActivity(intent)
    }
}