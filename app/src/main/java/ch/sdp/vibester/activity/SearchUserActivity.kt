package ch.sdp.vibester.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.user.User

import ch.sdp.vibester.user.UserProfileAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    lateinit var imageGetter: ImageGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    var users = arrayListOf<User>()
    lateinit var userProfileAdapter: UserProfileAdapter

    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null

    private var uidList: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search_user)

        recyclerView = findViewById(R.id.searchList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        userProfileAdapter = UserProfileAdapter(this.users, authenticator, usersRepo, imageGetter)

        recyclerView!!.adapter = userProfileAdapter

        searchEditText = findViewById(R.id.searchUserET)
        searchForUsers("")

        val buttonScan: FloatingActionButton = findViewById(R.id.searchUser_scanning)

        val extras = intent.extras

        buttonScan.setOnClickListener {
            val qrIntent = Intent(this, QrScanningActivity::class.java)
            qrIntent.putExtra("uidList", uidList)
            if (extras != null) {
                qrIntent.putExtra("isTest", extras.getBoolean("isTest", false))
            }
            startActivity(qrIntent)
        }

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
     * Callback to update users in adapter during search
     */
    private fun setUserInAdapter2(users: ArrayList<String> = ArrayList()) {
        uidList = ArrayList(users)
    }

    /**
     * Search for users by usernames in Firebase Realtime Database
     * @param inputUsername search text inputed by user
     */
    private fun searchForUsers(inputUsername:String){
        usersRepo.searchByField("username", inputUsername, callback = ::setUserInAdapter, callbackUid = ::setUserInAdapter2)
    }
}


