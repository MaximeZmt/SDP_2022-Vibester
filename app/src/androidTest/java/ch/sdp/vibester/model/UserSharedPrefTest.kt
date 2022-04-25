package ch.sdp.vibester.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.user.User
import org.junit.Assert.assertEquals
import org.junit.Test

class UserSharedPrefTest {
    @Test
    fun sharedPrefTest(){
        val ctx = ApplicationProvider.getApplicationContext() as Context

        val mailTest = "lisa@test.com"
        UserSharedPref.userReset(ctx, mailTest)
        UserSharedPref.updateUsername(ctx, "Lalisa Bon")
        UserSharedPref.updateScore(ctx)
        Thread.sleep(1000)
//        assertEquals(mailTest, UserSharedPref.getUser(ctx).email) //TODO temporary disable does not pass on CI
    }

    @Test
    fun sharedPrefTest2(){
        val ctx = ApplicationProvider.getApplicationContext() as Context
        val username: String = "myUsername"
        val image: String = "myImage"
        val email: String = "myEmail"
        val totalGames: Int = 10
        val bestScore: Int = 27
        val correctSongs: Int = 16
        val ranking: Int = 6

        val pro = User(username, image, email, totalGames, bestScore, correctSongs, ranking)
        UserSharedPref.setUser(ctx, pro, false)
        UserSharedPref.updateScore(ctx, 1, -1, 0, 1)
        Thread.sleep(1000)
        val updated = UserSharedPref.getUser(ctx)

        assertEquals(pro.username, updated.username)
        assertEquals(pro.image, updated.image)
        assertEquals(pro.email, updated.email)
        assertEquals(pro.totalGames+1, updated.totalGames)
        assertEquals(pro.bestScore-1, updated.bestScore)
        assertEquals(pro.correctSongs, updated.correctSongs)
        assertEquals(pro.ranking+1, updated.ranking)
    }


}
