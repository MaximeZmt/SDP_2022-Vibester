package ch.sdp.vibester.activity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.user.User
import ch.sdp.vibester.user.UserScoreboardAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ScoreBoardActivity : AppCompatActivity() {
    private val dbRef: DatabaseReference = Database.get().getReference("users")
    private var players: List<User>? = null
    private var userScoreboardAdapter: UserScoreboardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoreboard_scoreboard)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupRecycleView()
        players = ArrayList()
        loadPlayers()
    }

    private fun setupRecycleView() {
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = players?.let { UserScoreboardAdapter(it) }
            setHasFixedSize(true)
        }
    }

    private fun loadPlayers() {
        dbRef.orderByChild("ranking")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                for (snapshot in snapshots.children) {
                    val player: User? = snapshot.getValue(User::class.java)
                    if (player != null) {
                        (players as? ArrayList<User>)?.add(player)
                    }
                }
                showPlayersPosition(players)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPlayers:onCancelled", error.toException()) }
        })
    }

    private fun showPlayersPosition(players: List<User>?) {
        userScoreboardAdapter = UserScoreboardAdapter(players!!)
        findViewById<RecyclerView>(R.id.recycler_view)!!.adapter = userScoreboardAdapter
    }
}