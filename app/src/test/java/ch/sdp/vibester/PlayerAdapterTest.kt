package ch.sdp.vibester

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.scoreboard.Player
import ch.sdp.vibester.scoreboard.PlayerAdapter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(maxSdk = 31)
class PlayerAdapterTest {
    private lateinit var adapter: PlayerAdapter
    private lateinit var holder: PlayerAdapter.PlayerViewHolder
    private lateinit var mockView: View

    @Rule //initMocks
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        ApplicationProvider.getApplicationContext<Context>()
        adapter = PlayerAdapter()
        mockView = mock(View::class.java)
    }

    @Test
    fun getItemCountTest() {
        val player = Player(111, "Brownie","https://images.app.goo.gl/yiPpy7JDRFaZRiAg9", 687 )
        adapter.addPlayers(listOf(player))
        assertEquals(1, adapter.itemCount)
    }
}