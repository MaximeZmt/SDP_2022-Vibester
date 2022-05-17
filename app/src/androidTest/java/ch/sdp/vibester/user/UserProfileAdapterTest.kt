package ch.sdp.vibester.user

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class UserProfileAdapterTest {

    @Test
    fun recyclerViewShowsCorrectCount() {
        val user1 = User("test1",  "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2",  "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<User> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val userProfileViewHolder: RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> =
            UserProfileAdapter(users, FireBaseAuthenticator(), DataGetter(), ImageGetter(), null)
        assertThat(userProfileViewHolder.itemCount, equalTo(users.size))
    }

    @Test
    fun itemTypeIsCorrect() {
        val user1 = User("test1", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val user2 = User("test2", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val users: MutableList<User> = arrayListOf()
        users.addAll(listOf(user1, user2))
        val userProfileViewHolder: RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> =
            UserProfileAdapter(users, FireBaseAuthenticator(), DataGetter(), ImageGetter(), null)
        val defaultType = 0
        assertThat(
            userProfileViewHolder.getItemViewType(0),
            equalTo(defaultType)
        )
    }
}