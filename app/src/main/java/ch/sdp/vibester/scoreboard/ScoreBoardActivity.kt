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

    // temporary hard-coded list of players for display
    private val players: List<Player> = listOf(
        Player(111, "Brownie","https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", 687 ),
        Player(555, "Cheesecake", "https://images.app.goo.gl/REJnoWR2t3mi2kYJA", 678),
        Player(444, "Scone", "https://images.app.goo.gl/YkBi16zwyjB7ejj96", 659),
        Player(333, "Cookie", "https://images.app.goo.gl/49955BeModw7Q1Ls5", 593),
        Player(981, "Cinnamon Roll", "https://images.app.goo.gl/o8oYg2jAjQc757nn6", 568),
        Player(267, "Fruits Tart", "https://images.app.goo.gl/uMstJPy6SrbpZC3v6", 523),
        Player(222, "Pancake", "https://images.app.goo.gl/3BAnNtTn6isYGgQX9", 498),
        Player(143, "Ice Cream", "https://images.app.goo.gl/f2iKsWuhQYaB9GB58", 450),
        Player(528, "Waffle", "https://images.app.goo.gl/jsjRdqah1RfMtfXM8", 439),
        Player(628, "Macaron", "https://images.app.goo.gl/J6bm9BqNTiKPwDju9", 412),
        Player(729, "Croissant", "https://images.app.goo.gl/WVAL5WaWRUW2gFys8", 385),
        Player(963, "Pudding", "https://images.app.goo.gl/UpXEDTFLbcTL5mTJ8", 127),
    )

    private fun setupRecycleView() {
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PlayerAdapter(players)
            setHasFixedSize(true)
        }
    }

    private fun loadPlayers() {
        //TODO: replace hard-coded players by firebase query
        showPlayersPosition(players)
    }

    private fun showPlayersPosition(players: List<Player>) {
        val adapter = findViewById<RecyclerView>(R.id.recycler_view).adapter as PlayerAdapter
        adapter.addPlayers(players)
    }
}