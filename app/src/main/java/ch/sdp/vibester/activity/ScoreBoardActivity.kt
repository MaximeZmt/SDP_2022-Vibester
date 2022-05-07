package ch.sdp.vibester.activity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
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
    private var genre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoreboard_scoreboard)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupRecycleView()
        players = ArrayList()
    }

    fun selectScoreboard(view: View) {
        var sortedBy = "scores/"
        when (view.id) {
            R.id.btsButton -> {sortedBy += R.string.bts; genre = R.string.bts.toString()}
            R.id.kpopButton -> {sortedBy += R.string.kpop; genre = R.string.kpop.toString()}
            R.id.imagDragonsButton -> {sortedBy += R.string.imagine_dragons; genre = R.string.imagine_dragons.toString()}
            R.id.billieEilishButton -> {sortedBy += R.string.billie_eilish; genre = R.string.billie_eilish.toString()}
            R.id.rockButton -> {sortedBy += R.string.rock; genre = R.string.rock.toString()}
            R.id.topTracksButton -> {sortedBy += R.string.top_tracks; genre = R.string.top_tracks.toString()}
        }

        findViewById<ConstraintLayout>(R.id.genrePerScoreboard).visibility = GONE
        findViewById<NestedScrollView>(R.id.scoreboard_content_scrolling).visibility = VISIBLE

        loadPlayersSortedBy(sortedBy)
    }

    private fun setupRecycleView() {
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = players?.let { UserScoreboardAdapter(it, genre) }
            setHasFixedSize(true)
        }
    }

    private fun loadPlayersSortedBy(genre: String) {
        dbRef.orderByChild(genre)
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
        userScoreboardAdapter = UserScoreboardAdapter(players!!, genre)
        findViewById<RecyclerView>(R.id.recycler_view)!!.adapter = userScoreboardAdapter
    }
}