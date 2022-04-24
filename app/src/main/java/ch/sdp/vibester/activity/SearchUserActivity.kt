package ch.sdp.vibester.activity

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.database.UsersRepo
import ch.sdp.vibester.profile.UserProfile
import ch.sdp.vibester.profile.UserProfileAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Search for users based on their usernames.
 */
@AndroidEntryPoint
class SearchUserActivity : AppCompatActivity() {
    private var userProfileAdapter: UserProfileAdapter? = null
    private var users: MutableList<UserProfile>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null
    @Inject
    lateinit var usersRepo: UsersRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search_user)

        recyclerView = findViewById(R.id.searchList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        users = ArrayList()

        searchEditText = findViewById(R.id.searchUserET)
        searchForUsers("")

        searchEditText!!.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchForUsers(p0.toString())
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    /**
     * Callback to update users in adapter during search
     */
    private fun setUserInAdapter(){
//        println(users)
        userProfileAdapter = UserProfileAdapter(users!!)
        recyclerView!!.adapter = userProfileAdapter
    }

    /**
     * Search for users by usernames in Firebase Realtime Database
     * @param inputUsername search text inputed by user
     */
    private fun searchForUsers(inputUsername:String){
        usersRepo.searchByField("username", inputUsername,
            users as ArrayList<UserProfile>, callback = setUserInAdapter())
    }
}

