package ch.sdp.vibester.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ch.sdp.vibester.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //Getting the Navigation Controller
        navController = Navigation.findNavController(this, R.id.fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        //Setting the navigation controller to Bottom Nav
        bottomNav.setupWithNavController(navController)


        //Setting up the action bar
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    //Setting Up the back button
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, fragment, "TEST")
            .commit()
    }
}