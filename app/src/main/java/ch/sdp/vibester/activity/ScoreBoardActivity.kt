package ch.sdp.vibester.activity

import android.content.ContentValues
import android.content.Intent
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
import ch.sdp.vibester.activity.profile.PublicProfileActivity
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.user.OnItemClickListener
import ch.sdp.vibester.user.User
import ch.sdp.vibester.user.UserScoreboardAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreBoardActivity : AppCompatActivity(), OnItemClickListener {
    private val dbRef: DatabaseReference = Database.get().getReference("users")
    private var players: MutableList<User>? = null
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
        // can't use R.string for genre (getOrDefault in setScore in UserScoreboardAdapter)
        when (view.id) {
            R.id.btsButton -> {sortedBy += R.string.bts; genre = "BTS"}
            R.id.kpopButton -> {sortedBy += R.string.kpop; genre = "kpop"}
            R.id.imagDragonsButton -> {sortedBy += R.string.imagine_dragons; genre = "Imagine Dragons"}
            R.id.billieEilishButton -> {sortedBy += R.string.billie_eilish; genre = "Billie Eilish"}
            R.id.rockButton -> {sortedBy += R.string.rock; genre = "rock"}
            R.id.topTracksButton -> {sortedBy += R.string.top_tracks; genre = "top tracks"}
        }

        findViewById<ConstraintLayout>(R.id.genrePerScoreboard).visibility = GONE
        findViewById<NestedScrollView>(R.id.scoreboard_content_scrolling).visibility = VISIBLE

        loadPlayersSortedBy(sortedBy)
    }

    private fun setupRecycleView() {
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = players?.let { UserScoreboardAdapter(it, genre, this@ScoreBoardActivity) }
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
                players = players?.let { replaceRankingByScore(it) }
                players = players?.sortedByDescending { it.ranking } as MutableList<User>?
                showPlayersPosition(players)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPlayers:onCancelled", error.toException()) }
        })
    }

    private fun replaceRankingByScore(list: MutableList<User>): MutableList<User> {
        val iterator = list.listIterator()
        while(iterator.hasNext()) {
            val player = iterator.next()
            player.ranking = player.scores.getOrDefault(genre, 0)
            iterator.set(player)
        }
        return list
    }

    private fun showPlayersPosition(players: MutableList<User>?) {
        userScoreboardAdapter = UserScoreboardAdapter(players!!, genre, this)
        findViewById<RecyclerView>(R.id.recycler_view)!!.adapter = userScoreboardAdapter
    }

    /**
     * @param position index of the item on click
     * go to the profile of the player at index position
     */
    override fun onItemClick(position: Int) {
        val intent = Intent(this, PublicProfileActivity::class.java)
        intent.putExtra("UserId", players?.get(position)?.uid)
        intent.putExtra("ScoresOrFriends", "Scores" )
        startActivity(intent)
    }
}