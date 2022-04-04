package ch.sdp.vibester.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.profile.UserProfile
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class UserSharedPrefTest {
    @Test
    fun sharedPrefTest(){
        val handle: String = "myHandle"
        val username: String = "myUsername"
        val image: String = "myImage"
        val email: String = "myEmail"
        val totalGames: Int = 10
        val bestScore: Int = 27
        val correctSongs: Int = 16
        val ranking: Int = 6

        val ctx = ApplicationProvider.getApplicationContext() as Context

        val pro = UserProfile(handle, username, image, email, totalGames, bestScore, correctSongs, ranking)
        UserSharedPref.setUser(ctx, pro)
        UserSharedPref.updateScore(ctx, 1, -1, 0, 1)

        val updated = UserSharedPref.getUser(ctx)

        assertEquals(pro.handle, updated.handle)
        assertEquals(pro.username, updated.username)
        assertEquals(pro.image, updated.image)
        assertEquals(pro.email, updated.email)
        assertEquals(pro.totalGames+1, updated.totalGames)
        assertEquals(pro.bestScore-1, updated.bestScore)
        assertEquals(pro.correctSongs, updated.correctSongs)
        assertEquals(pro.ranking+1, updated.ranking)

    }
}