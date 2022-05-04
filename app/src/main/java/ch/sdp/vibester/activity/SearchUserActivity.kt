package ch.sdp.vibester.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.user.User

import ch.sdp.vibester.user.UserProfileAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Search for users based on their usernames.
 */
@AndroidEntryPoint
class SearchUserActivity : AppCompatActivity() {

    @Inject
    lateinit var usersRepo: DataGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    var users = arrayListOf<User>()
    lateinit var userProfileAdapter: UserProfileAdapter

    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search_user)

        recyclerView = findViewById(R.id.searchList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        userProfileAdapter = UserProfileAdapter(this.users, authenticator, usersRepo)

        recyclerView!!.adapter = userProfileAdapter

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
    private fun setUserInAdapter(users: ArrayList<User> = ArrayList()) {
        userProfileAdapter.updateUsersList(users)
        userProfileAdapter.notifyDataSetChanged()
    }

    /**
     * Search for users by usernames in Firebase Realtime Database
     * @param inputUsername search text inputed by user
     */
    private fun searchForUsers(inputUsername:String){
        usersRepo.searchByField("username", inputUsername, callback = ::setUserInAdapter)
    }
}


