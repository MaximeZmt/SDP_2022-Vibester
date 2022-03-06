package ch.sdp.vibester.scoreboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R

class ScoreBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoreboard_scoreboard)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupRecycleView()
        loadPlayers()
    }

    private fun setupRecycleView() {
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PlayerAdapter()
            setHasFixedSize(true)
        }
    }

    private fun loadPlayers() {
        //TODO: replace by firebase
        val players: List<Player> = listOf(
            Player(111, "Brownie","", 687 ),
            Player(222, "pancake", "", 498),
            Player(333, "cookie", "", 593),
            Player(444, "Scone", "", 659),
            Player(555, "cheesecake", "", 678),
        )
        showPlayersPosition(players)
    }

    private fun showPlayersPosition(players: List<Player>) {
        val adapter = findViewById<RecyclerView>(R.id.recycler_view).adapter as PlayerAdapter
        adapter.addPlayers(players)
    }
}