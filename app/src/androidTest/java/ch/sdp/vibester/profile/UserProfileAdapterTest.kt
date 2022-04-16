package ch.sdp.vibester.profile

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class UserProfileAdapterTest {
    @Test
    fun recyclerViewShowsCorrectCount() {
        val user1 = UserProfile("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = UserProfile("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<UserProfile> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val userProfileViewHolder: RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> = UserProfileAdapter(users)
        MatcherAssert.assertThat(userProfileViewHolder.itemCount, CoreMatchers.equalTo(2))
    }

    @Test
    fun itemTypeIsCorrect() {
        val user1 = UserProfile("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = UserProfile("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<UserProfile> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val userProfileViewHolder: RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> = UserProfileAdapter(users)
        val defaultType = 0
        MatcherAssert.assertThat(
            userProfileViewHolder.getItemViewType(0),
            CoreMatchers.equalTo(defaultType)
        )
    }

    @Test
    fun setupAdapterForRecyclerView() {
        val user1 = UserProfile("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = UserProfile("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<UserProfile> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        recyclerView.layoutManager =
            LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val userAdapter = UserProfileAdapter(users)
        recyclerView.adapter = userAdapter
        val newUsers: MutableList<UserProfile> = arrayListOf()
        newUsers.add(UserProfile("test3", "Cake", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test3@gmail.com"))
        userAdapter.addUsers(newUsers)
        userAdapter.notifyDataSetChanged()
    }

    @Test
    fun addPlayersWorks() {
        val user1 = UserProfile("test1", "Brownie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = UserProfile("test2", "Cookie", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<UserProfile> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
        recyclerView.layoutManager =
            LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val userAdapter = UserProfileAdapter(users)
        val user3 = UserProfile("test3", "Cake", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test3@gmail.com")
        val updatedList = arrayListOf(user3)
        userAdapter.addUsers(updatedList)
        MatcherAssert.assertThat(userAdapter.users == updatedList, CoreMatchers.equalTo(true))
    }
}