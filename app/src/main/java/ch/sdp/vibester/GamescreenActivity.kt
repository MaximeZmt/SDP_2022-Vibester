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

        val buzzers = findViewById<LinearLayout>(R.id.buzzersLayout)
        val scores = findViewById<LinearLayout>(R.id.scoresTable)

        val gamescreenIntent = Intent(this, GamescreenActivity::class.java)

        val buildPopup = AlertDialog.Builder(this)


        var i = 1

        val buttons = arrayOfNulls<Button>(players.size)

        val viewsOfPoints = arrayOfNulls<TableRow>(players.size)
        val allPoints = arrayOf(1, 2, 3, 4)

        for (pName in players) {

            //val scoreName = "score" + i.toString()
            //val scoreId = resources.getIdentifier(scoreName, "id", packageName)
            val score = TableRow(this)
            //score.id = scoreId
            score.gravity = Gravity.CENTER
            val nameView = TextView(this)
            nameView.text = pName
            nameView.height = 75
            nameView.width = 300
            nameView.gravity = Gravity.LEFT
            val points = TextView(this)
            points.text = allPoints[i-1].toString()
            points.height = 75
            points.width = 150
            points.gravity = Gravity.RIGHT
            score.addView(nameView)
            score.addView(points)
            viewsOfPoints.set(i-1, score)
            scores.addView(score)//,TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT))
            //val buttonName = "button" + i.toString()
            //val buttonId = resources.getIdentifier(buttonName, "id", packageName)
            val button = Button(this)
            button.text = pName
            //button.id = buttonId
            button.width = 0
            button.height = 150
            buttons.set(i-1, button)
            button.setOnClickListener {

                //startActivity(gamescreenIntent)
            }
            buzzers.addView(button)

            i = i + 1
        }


    }
}