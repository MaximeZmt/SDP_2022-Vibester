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
import ch.sdp.vibester.BuzzerScoreUpdater
import ch.sdp.vibester.R


class BuzzerScreenActivity : AppCompatActivity() {

    private val MAX_N_PLAYERS = 4
    private val NO_BUZZER_PRESSED = -1
    private val rowsIdArray = arrayOf(R.id.row_0, R.id.row_1, R.id.row_2, R.id.row_3)
    private val buzIds = arrayOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)

    var pressedBuzzer = NO_BUZZER_PRESSED

    private fun setPressed(id: Int) {
        pressedBuzzer = id
    }

    private fun fetchBuzToScoreRowMap(): Map<Int, Int> {

        var theMap = LinkedHashMap<Int, Int>()
        var i = 0
        while (i < buzIds.size) {
            theMap.put(buzIds[i], rowsIdArray[i])
            i = i + 1
        }
        return theMap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_buzzer_screen)

        val getIntent = intent.extras
        val nPlayers = getIntent?.getInt("Number of players")

        val answer = findViewById<LinearLayout>(R.id.answer)
        val answerText = findViewById<TextView>(R.id.answerText)
        answerText.text = "The song was Demo by The Placeholders"

        val allPoints = if (nPlayers!=null) {Array<Int>(nPlayers, { i -> 0 }) } else Array<Int>(MAX_N_PLAYERS, {i ->0})

        val playersFull = getIntent?.getStringArray("Player Names")
        val players = nPlayers?.let { playersFull?.copyOfRange(0, it) }

        val updater = BuzzerScoreUpdater(buzIds, allPoints)

        val buzToScoreMap = fetchBuzToScoreRowMap()

        if (players != null) {
            buildScores(players, allPoints)
            buildBuzzers(players, answer)
        }
        setAnswerButton(answer, findViewById(R.id.buttonCorrect), updater, buzToScoreMap)
        setAnswerButton(answer, findViewById(R.id.buttonWrong), updater, buzToScoreMap)
    }



    /**
     * Programmatically builds the table of scores according to the number of players
     * @param players: an array of player names
     * @param allPoints: an array of points/scores, all set to 0 before the function is called
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
            points.id=rowsIdArray[i]

            score.addView(nameView)
            score.addView(points)
            viewsOfPoints.set(i, score)
            scores.addView(score)

            i = i + 1
        }
    }

    /**
     * Programmatically builds the buzzers according to the number and names of players.
     * @param players: an array of player names
     * @param answer: the answer layout
     */
    private fun buildBuzzers(players: Array<String>, answer: LinearLayout) {

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
                setPressed(button.id)
            }
            buzzers.addView(button)
            i = i + 1
        }
    }

    /**
     * Connects the answer buttons to the answer layout's visibility
     * @param answer: the answer layout
     * @param button: the answer button to be set
     * @param updater: the updater for the scores
     * @param map: a map from the buzzers' IDs to the IDs of each score's position in the score table layout
     */
    private fun setAnswerButton(answer: LinearLayout, button: Button, updater: BuzzerScoreUpdater, map: Map<Int, Int>) {
        button.setOnClickListener {
            answer.visibility = android.view.View.INVISIBLE
            if (button.id==R.id.buttonCorrect) {
                if (pressedBuzzer >= 0) {
                    updater.updateScoresArray(pressedBuzzer)
                    val view = map[pressedBuzzer]?.let { it1 -> findViewById<TextView>(it1) }
                    if (view != null && updater.getMap().keys.contains(pressedBuzzer)) {view.text=updater.getMap()[pressedBuzzer].toString()}
                }
            }
        }
        setPressed(NO_BUZZER_PRESSED) // reset the buzzer
    }


    /**
     * Fires an intent from the Gamescreen to the Ending Screen
     */
    fun switchToEnding(view: View) {
        val mockArray = arrayListOf<String>("One", "Two", "Three", "Four", "Five")
        val intent = Intent(this, GameEndingActivity::class.java)
        
        val incArray: ArrayList<String> = mockArray
        val statNames: ArrayList<String> = mockArray
        val statVal: ArrayList<String> = mockArray

        intent.putExtra("playerName", "Arda")
        intent.putExtra("nbIncorrectSong", 3)

        intent.putStringArrayListExtra("str_arr_inc", incArray)
        intent.putStringArrayListExtra("str_arr_name", statNames)
        intent.putStringArrayListExtra("str_arr_val", statVal)

        startActivity(intent)
    }
}