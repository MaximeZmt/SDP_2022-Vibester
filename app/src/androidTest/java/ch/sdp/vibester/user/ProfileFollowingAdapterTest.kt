package ch.sdp.vibester.user

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

class ProfileFollowingAdapterTest {
    @Test
    fun recycleViewShowsCorrectCount() {
        val friend1 = User("test1",  "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val friend2 = User("test2",  "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val friends: MutableList<User> = arrayListOf()
        friends.addAll(listOf(friend1, friend2))
        val profileFollowingViewHolder: RecyclerView.Adapter<ProfileFollowingAdapter.ProfileFollowingViewHolder> =
            ProfileFollowingAdapter(friends, DataGetter(), FireBaseAuthenticator(), null)
        assertThat(profileFollowingViewHolder.itemCount, equalTo(friends.size))
    }

    @Test
    fun itemTypeIsCorrect() {
        val friend1 = User("test1", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test1@gmail.com")
        val friend2 = User("test2", "https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", "test2@gmail.com")
        val friends: MutableList<User> = arrayListOf()
        friends.addAll(listOf(friend1, friend2))
        val profileFollowingViewHolder: RecyclerView.Adapter<ProfileFollowingAdapter.ProfileFollowingViewHolder> =
            ProfileFollowingAdapter(friends, DataGetter(), FireBaseAuthenticator(), null)
        val defaultType = 0
        assertThat(
            profileFollowingViewHolder.getItemViewType(0),
            equalTo(defaultType)
        )
    }

}