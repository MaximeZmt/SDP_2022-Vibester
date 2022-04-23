package ch.sdp.vibester.scoreboard

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.user.User
import ch.sdp.vibester.user.UserScoreboardAdapter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class UserScoreboardAdapterTest {
    @Test
    fun recyclerViewShowsCorrectCount() {
        val user1 = User("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val players: MutableList<User> = arrayListOf()
        players.addAll(listOf(user1, user2))
        val userScoreboardViewHolder: RecyclerView.Adapter<UserScoreboardAdapter.PlayerViewHolder> =
            UserScoreboardAdapter(players)
        assertThat(userScoreboardViewHolder.itemCount, equalTo(2))
    }

    @Test
    fun itemTypeIsCorrect() {
        val user1 = User("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val players: MutableList<User> = arrayListOf()
        players.addAll(listOf(user1, user2))
        val userScoreboardViewHolder: RecyclerView.Adapter<UserScoreboardAdapter.PlayerViewHolder> =
            UserScoreboardAdapter(players)
        val defaultType = 0
        assertThat(userScoreboardViewHolder.getItemViewType(0), equalTo(defaultType))
    }

    @Test
    fun setupAdapterForRecyclerView() {
        val user1 = User("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val players: MutableList<User> = arrayListOf()
        players.addAll(listOf(user1, user2))
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        recyclerView.layoutManager =
            LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val playerAdapter = UserScoreboardAdapter(players)
        recyclerView.adapter = playerAdapter
        val newPlayers: MutableList<User> = arrayListOf()
        newPlayers.add(User("test3", "Scone", "https://images.app.goo.gl/YkBi16zwyjB7ejj96", "test3@gmail.com"))
        playerAdapter.addPlayers(newPlayers)
        playerAdapter.notifyDataSetChanged()
    }

    @Test
    fun addPlayersWorks() {
        val user1 = User("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val players: MutableList<User> = arrayListOf()
        players.addAll(listOf(user1, user2))
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        recyclerView.layoutManager =
            LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val playerAdapter = UserScoreboardAdapter(players)
        val player3 = User("test3", "Scone", "https://images.app.goo.gl/YkBi16zwyjB7ejj96", "test3@gmail.com")
        val updatedList = arrayListOf(player3)
        playerAdapter.addPlayers(updatedList)
        assertThat(playerAdapter.players == updatedList, equalTo(true))
    }
}