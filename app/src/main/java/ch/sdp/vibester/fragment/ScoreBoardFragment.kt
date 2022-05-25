package ch.sdp.vibester.fragment

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.profile.PublicProfileActivity
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.user.OnItemClickListener
import ch.sdp.vibester.user.User
import ch.sdp.vibester.user.UserScoreboardAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScoreBoardFragment : Fragment(R.layout.fragment_scoreboard), OnItemClickListener {
    private val dbRef: DatabaseReference = Database.get().getReference("users")
    private var players: MutableList<User> = mutableListOf()
    private var userScoreboardAdapter: UserScoreboardAdapter? = null
    private var genre: String = ""

    @Inject
    lateinit var imageGetter: ImageGetter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = view.context

        view.findViewById<Button>(R.id.scoreboard_kpopButton).setOnClickListener { setGenreListeners(view, "Kpop") }
        view.findViewById<Button>(R.id.scoreboard_rockButton).setOnClickListener {setGenreListeners(view, "Rock") }
        view.findViewById<Button>(R.id.scoreboard_btsButton).setOnClickListener { setGenreListeners(view, "BTS") }
        view.findViewById<Button>(R.id.scoreboard_topTracksButton).setOnClickListener{ setGenreListeners(view, "top tracks") }
        view.findViewById<Button>(R.id.scoreboard_imagDragonsButton).setOnClickListener{ setGenreListeners(view, "Imagine Dragons") }
        view.findViewById<Button>(R.id.scoreboard_billieEilishButton).setOnClickListener { setGenreListeners(view, "Billie Eillish") }
        setupRecycleView(view, ctx)
    }

    private fun selectScoreboard(view: View) {
        val sortedBy = "scores/$genre"

        view.findViewById<ConstraintLayout>(R.id.genrePerScoreboard).visibility = GONE
        view.findViewById<NestedScrollView>(R.id.scoreboard_content_scrolling).visibility = VISIBLE

        loadPlayersSortedBy(sortedBy, view)
    }

    private fun setupRecycleView(view:View, context: Context) {
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserScoreboardAdapter(players, genre, this@ScoreBoardFragment, imageGetter)
            setHasFixedSize(true)
        }
    }

    private fun loadPlayersSortedBy(genre: String, view: View) {
        dbRef.orderByChild(genre)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                for (snapshot in snapshots.children) {
                    val player: User? = snapshot.getValue(User::class.java)
                    if (player != null) {
                        (players as? ArrayList<User>)?.add(player)
                    }
                }
                players = replaceRankingByScore(players)
                players = players.sortedByDescending { it.ranking } as MutableList<User>
                showPlayersPosition(players, view)
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

    private fun showPlayersPosition(players: MutableList<User>, view: View) {
        userScoreboardAdapter = UserScoreboardAdapter(players, genre, this, imageGetter)
        view.findViewById<RecyclerView>(R.id.recycler_view)!!.adapter = userScoreboardAdapter
    }

    /**
     * @param position index of the item on click
     * go to the profile of the player at index position
     */
    override fun onItemClick(position: Int) {
        val intent = Intent(requireActivity(), PublicProfileActivity::class.java)
        intent.putExtra("UserId", players[position].uid)
        intent.putExtra("ScoresOrFollowing", R.string.profile_scores.toString() )
        startActivity(intent)
    }

    private fun setGenreListeners(view: View, genre: String){
        this.genre = genre
        selectScoreboard(view)
    }
}