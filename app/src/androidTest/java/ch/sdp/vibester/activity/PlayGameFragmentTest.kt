//package ch.sdp.vibester.activity
//
//import androidx.fragment.app.testing.launchFragmentInContainer
//import androidx.lifecycle.Lifecycle
//import androidx.test.espresso.Espresso
//import androidx.test.espresso.assertion.ViewAssertions
//import androidx.test.espresso.matcher.ViewMatchers
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import ch.sdp.vibester.R
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class PlayGameFragmentTest {
//
//    @Before
//    fun setup() {
////        launchFragmentInContainer<PlayGameFragment>(
////            fragmentArgs = null, // Bundle
////            themeResId = R.style.AppTheme,
////            initialState = Lifecycle.State.RESUMED, // Lifecycle.State
////            factory = null // FragmentFactory
////        )
//        // or
//        // short version
//        launchFragmentInContainer<PlayGameFragment>(
//            themeResId = R.style.AppTheme
//        )
//    }
//
//    @Test
//    fun checkDefaultSelectDifficulty() {
//        Espresso.onView(ViewMatchers.withId(R.id.difficulty_spinner))
//            .check(ViewAssertions.matches(ViewMatchers.withSpinnerText(R.string.easy)))
//    }
//
//    @Test
//    fun checkDefaultSelectGameSize() {
//        Espresso.onView(ViewMatchers.withId(R.id.size_spinner))
//            .check(ViewAssertions.matches(ViewMatchers.withSpinnerText(R.string.one)))
//    }
//}