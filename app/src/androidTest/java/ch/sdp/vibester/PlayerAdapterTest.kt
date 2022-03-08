package ch.sdp.vibester

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.scoreboard.Player
import ch.sdp.vibester.scoreboard.PlayerAdapter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class PlayerAdapterTest {
    @Test
    fun recyclerViewShowsCorrectCount() {
        val player1 = Player(111, "Brownie","https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", 687 )
        val player2 = Player(555, "Cheesecake", "https://images.app.goo.gl/REJnoWR2t3mi2kYJA", 678)
        val players: MutableList<Player> = arrayListOf()
        players.addAll(listOf(player1, player2))
        val playerViewHolder: RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> = PlayerAdapter(players)
        assertThat(playerViewHolder.itemCount, equalTo(2))
    }

    @Test
    fun itemTypeIsCorrect() {
        val player1 = Player(111, "Brownie","https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", 687 )
        val player2 = Player(555, "Cheesecake", "https://images.app.goo.gl/REJnoWR2t3mi2kYJA", 678)
        val players: MutableList<Player> = arrayListOf()
        players.addAll(listOf(player1, player2))
        val playerViewHolder: RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> = PlayerAdapter(players)
        val defaultType = 0
        assertThat(playerViewHolder.getItemViewType(0), equalTo(defaultType))
    }

    @Test
    fun setupAdapterForRecyclerView() {
        val player1 = Player(111, "Brownie","https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", 687 )
        val player2 = Player(555, "Cheesecake", "https://images.app.goo.gl/REJnoWR2t3mi2kYJA", 678)
        val players: MutableList<Player> = arrayListOf()
        players.addAll(listOf(player1, player2))
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        recyclerView.layoutManager = LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val playerAdapter = PlayerAdapter(players)
        recyclerView.adapter = playerAdapter
        val newPlayers: MutableList<Player> = arrayListOf()
        newPlayers.add(Player(444, "Scone", "https://images.app.goo.gl/YkBi16zwyjB7ejj96", 659))
        playerAdapter.addPlayers(newPlayers)
        playerAdapter.notifyDataSetChanged()
    }

    @Test
    fun addPlayersWorks() {
        val player1 = Player(111, "Brownie","https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", 687 )
        val player2 = Player(555, "Cheesecake", "https://images.app.goo.gl/REJnoWR2t3mi2kYJA", 678)
        val players: MutableList<Player> = arrayListOf()
        players.addAll(listOf(player1, player2))
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        recyclerView.layoutManager = LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val playerAdapter = PlayerAdapter(players)
        val player3 = Player(444, "Scone", "https://images.app.goo.gl/YkBi16zwyjB7ejj96", 659)
        val updatedList = arrayListOf(player3)
        playerAdapter.addPlayers(updatedList)
        assertThat(playerAdapter.players == updatedList, equalTo(true))
    }
}