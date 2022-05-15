package ch.sdp.vibester.user

import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class UserProfileAdapterTest {

    @Test
    fun recyclerViewShowsCorrectCount() {
        val user1 = User("test1",  "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2",  "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<User> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val userProfileViewHolder: RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> = UserProfileAdapter(users, FireBaseAuthenticator(), DataGetter(), null)
        MatcherAssert.assertThat(userProfileViewHolder.itemCount, CoreMatchers.equalTo(2))
    }

    @Test
    fun itemTypeIsCorrect() {
        val user1 = User("test1", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<User> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val userProfileViewHolder: RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> = UserProfileAdapter(users, FireBaseAuthenticator(), DataGetter(), null)
        val defaultType = 0
        MatcherAssert.assertThat(
            userProfileViewHolder.getItemViewType(0),
            CoreMatchers.equalTo(defaultType)
        )
    }
}