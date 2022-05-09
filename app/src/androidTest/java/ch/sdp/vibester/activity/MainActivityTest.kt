//package ch.sdp.vibester.activity
//
//import androidx.navigation.NavController
//import androidx.navigation.Navigation
//import androidx.navigation.testing.TestNavHostController
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.matcher.ViewMatchers
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.internal.runner.junit4.statement.UiThreadStatement
//import ch.sdp.vibester.R
//import org.hamcrest.MatcherAssert.assertThat
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class MainActivityTest {
//
//    @Rule
//    @JvmField
//    val activityRule = ActivityScenarioRule(MainActivity::class.java)
//
//    @After
//    fun clean() {
//        Intents.release()
//    }
//
//    @Before
//    fun setup() {
//        Intents.init()
//    }
//
//    @Test
//    fun testGameSetupFragmentClick() {
//        onView(ViewMatchers.withId(R.id.gameSetupBtn)).perform(ViewActions.click())
//        onView(ViewMatchers.withId(R.id.fragment_game_setup)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun testWelcomeScreenFragmentClick() {
//        onView(ViewMatchers.withId(R.id.welcomeScreenBtn)).perform(ViewActions.click())
//        onView(ViewMatchers.withId(R.id.fragment_welcome_screen)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun testWelcomeScreenFragmentOnStart() {
//        onView(ViewMatchers.withId(R.id.fragment_welcome_screen)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun clickOnDrawerMaps_NavigateToAboutAppFragment() {
//        //Create TestNavHostController
//        val testNavController = TestNavHostController(ApplicationProvider.getApplicationContext()) as NavController
//
//        UiThreadStatement.runOnUiThread { // This needed because it throws a exception that method addObserver must be called in main thread
//            testNavController.setGraph(R.navigation.bottom_navigation)
//        }
//
//        val scenario = activityRule.scenario
//        var navcontroller : NavController? = null
//        scenario.onActivity {mapsActivity ->
//            navcontroller = mapsActivity.navController //Get the real navController just to debug
//            mapsActivity.navController = testNavController //Set the test navController
//            Navigation.setViewNavController(mapsActivity.binding.containerFragment, testNavController)
//        }
//
////        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open()).check(matches(isOpen()))
//        onView(withId(R.id.gameSetupBtn)).perform(click())
//        assertEqual(testNavController.currentDestination?.id).isEqualTo(R.id.aboutAppFragment)
//    }
//
//
//}