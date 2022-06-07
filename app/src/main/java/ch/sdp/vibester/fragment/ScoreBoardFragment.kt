package ch.sdp.vibester.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.PublicProfileActivity
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.helper.IntentSwitcher
import ch.sdp.vibester.helper.ViewModel
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
class ScoreBoardFragment : Fragment(R.layout.fragment_layout_scoreboard), OnItemClickListener {
    private val dbRef: DatabaseReference = Database.get().getReference("users")
    private var players: MutableList<User> = mutableListOf()
    private var userScoreboardAdapter: UserScoreboardAdapter? = null
    private var genre: String = ""
    private var vmScoreBoard = ViewModel()
    @Inject
    lateinit var imageGetter: ImageGetter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmScoreBoard.view = view
        vmScoreBoard.ctx = view.context

        val kpop = view.findViewById<Button>(R.id.scoreboard_kpopButton)
        val rock = view.findViewById<Button>(R.id.scoreboard_rockButton)
        val bts = view.findViewById<Button>(R.id.scoreboard_btsButton)
        val topTracks = view.findViewById<Button>(R.id.scoreboard_topTracksButton)
        val imagDragons = view.findViewById<Button>(R.id.scoreboard_imagDragonsButton)
        val billieEilish = view.findViewById<Button>(R.id.scoreboard_billieEilishButton)

        kpop.setOnClickListener { setGenreListeners("Kpop") }
        rock.setOnClickListener {setGenreListeners("Rock") }
        bts.setOnClickListener { setGenreListeners("BTS") }
        topTracks.setOnClickListener{ setGenreListeners("top tracks") }
        imagDragons.setOnClickListener{ setGenreListeners("Imagine Dragons") }
        billieEilish.setOnClickListener { setGenreListeners("Billie Eillish") }
        setupRecycleView()
    }

    private fun selectScoreboard() {
        val sortedBy = "scores/$genre"

        vmScoreBoard.view.findViewById<ConstraintLayout>(R.id.genrePerScoreboard).visibility = GONE
        vmScoreBoard.view.findViewById<NestedScrollView>(R.id.scoreboard_content_scrolling).visibility = VISIBLE

        loadPlayersSortedBy(sortedBy)
    }

    private fun setupRecycleView() {
        vmScoreBoard.view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(vmScoreBoard.ctx)
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
                        (players as ArrayList<User>).add(player)
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


    private fun showPlayersPosition(players: MutableList<User>) {
        userScoreboardAdapter = UserScoreboardAdapter(players, genre, this, imageGetter)
        vmScoreBoard.view.findViewById<RecyclerView>(R.id.recycler_view).adapter = userScoreboardAdapter
    }

    /**
     * @param position index of the item on click
     * go to the profile of the player at index position
     */
    override fun onItemClick(position: Int) {
        val playerId = players[position].uid
        IntentSwitcher.switch(vmScoreBoard.ctx, PublicProfileActivity::class.java,
            Helper().goToPlayerProfileWithSection(playerId, true))
    }

    private fun setGenreListeners(genre: String){
        this.genre = genre
        selectScoreboard()
    }
}