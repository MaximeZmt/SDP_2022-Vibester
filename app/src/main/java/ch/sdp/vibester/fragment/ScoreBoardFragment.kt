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
import android.widget.LinearLayout
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
class ScoreBoardFragment : Fragment(), OnItemClickListener, View.OnClickListener{
    private val dbRef: DatabaseReference = Database.get().getReference("users")
    private var players: MutableList<User> = mutableListOf()
    private var userScoreboardAdapter: UserScoreboardAdapter? = null
    private var genre: String = ""

    @Inject
    lateinit var imageGetter: ImageGetter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scoreboard, container, false)
        val ctx = inflater.context

        view.findViewById<Button>(R.id.scoreboard_kpopButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.scoreboard_rockButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.scoreboard_btsButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.scoreboard_topTracksButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.scoreboard_imagDragonsButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.scoreboard_billieEilishButton).setOnClickListener(this)
        setupRecycleView(view, ctx)

        return view
    }

    private fun selectScoreboard() {
        val sortedBy = "scores/$genre"

        requireView().findViewById<ConstraintLayout>(R.id.genrePerScoreboard).visibility = GONE
        requireView().findViewById<NestedScrollView>(R.id.scoreboard_content_scrolling).visibility = VISIBLE

        loadPlayersSortedBy(sortedBy)
    }

    private fun setupRecycleView(view:View, context: Context) {
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserScoreboardAdapter(players, genre, this@ScoreBoardFragment, imageGetter)
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
                players = replaceRankingByScore(players)
                players = players.sortedByDescending { it.ranking } as MutableList<User>
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
        userScoreboardAdapter = UserScoreboardAdapter(players!!, genre, this, imageGetter)
        requireView().findViewById<RecyclerView>(R.id.recycler_view)!!.adapter = userScoreboardAdapter
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

    override fun onClick(v: View?) {
            when(v!!.id) {
                R.id.scoreboard_btsButton -> {
                    genre = "BTS"; selectScoreboard()
                }
                R.id.scoreboard_kpopButton -> {
                    genre = "kpop"; selectScoreboard()
                }
                R.id.scoreboard_imagDragonsButton -> {
                    genre = "Imagine Dragons"; selectScoreboard()
                }
                R.id.scoreboard_billieEilishButton -> {
                    genre = "Billie Eilish"; selectScoreboard()
                }
                R.id.scoreboard_rockButton -> {
                    genre = "rock";selectScoreboard()
                }
                R.id.scoreboard_topTracksButton -> {
                    genre = "top tracks";selectScoreboard()
                }

            }

    }
}