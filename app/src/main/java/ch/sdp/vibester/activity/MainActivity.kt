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
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity to maintain bottom navigation with fragments.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //Get the Navigation Controller
        navController = Navigation.findNavController(this, R.id.fragment)
        //Set the navigation controller to Bottom Nav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
       
        bottomNav.setupWithNavController(navController)


        //Set up the action bar
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    //Set up the back button
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}