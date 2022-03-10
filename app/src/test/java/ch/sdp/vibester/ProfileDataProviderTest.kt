package ch.sdp.vibester

import ch.sdp.vibester.profile.ProfileDataProvider
import ch.sdp.vibester.profile.UserProfile
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Test


class ProfileDataProviderTest {

    @Test
    fun checkGetUserData(){
        val userID = "0"
        val users: List<UserProfile> = listOf(
            UserProfile("user0", "username0","https://bit.ly/3tO0k8W",  5, 8, 34),
            UserProfile("user1", "username1", "https://bit.ly/3sZhbXm", 5, 8, 34)
        )
        val dataProvider = ProfileDataProvider(userID, users)
        val finalUser = dataProvider.getUserData()
        Assert.assertEquals(finalUser.bestScore, users[0].bestScore)
    }

}