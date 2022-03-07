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
        //TODO: replace hard-coded players by firebase query
        val players: List<Player> = listOf(
            Player(111, "Brownie","https://images.app.goo.gl/QJFUc8JwEs5aNHVJA", 687 ),
            Player(555, "cheesecake", "https://images.app.goo.gl/STS3nKqSLdxogekF7", 678),
            Player(444, "Scone", "https://images.app.goo.gl/WiSfZupnUQrMZZfs9", 659),
            Player(333, "cookie", "https://images.app.goo.gl/49955BeModw7Q1Ls5", 593),
            Player(222, "pancake", "https://images.app.goo.gl/3BAnNtTn6isYGgQX9", 498),
        )
        showPlayersPosition(players)
    }

    private fun showPlayersPosition(players: List<Player>) {
        val adapter = findViewById<RecyclerView>(R.id.recycler_view).adapter as PlayerAdapter
        adapter.addPlayers(players)
    }
}