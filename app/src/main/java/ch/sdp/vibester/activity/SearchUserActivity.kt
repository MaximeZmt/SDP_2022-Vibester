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
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.user.User
import ch.sdp.vibester.user.UserProfileAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

/**
 * Search for users based on their usernames.
 */
class SearchUserActivity : AppCompatActivity() {
    private var userProfileAdapter: UserProfileAdapter? = null
    private var users: MutableList<User>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null
    private val dbRef: DatabaseReference = Database.get().getReference("users")

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
     * Search for users by usernames in Firebase Realtime Database
     * @param inputUsername search text inputed by user
     */
    private fun searchForUsers(inputUsername:String){
        val queryUsers = dbRef
            .orderByChild("username")
            .startAt(inputUsername)
            .endAt(inputUsername+"\uf8ff")
        /** Comment about \uf8ff:
         * The \uf8ff character used in the query above is a very high code point in the Unicode range.
         * Because it is after most regular characters in Unicode, the query matches all values that start with a inputUsername. */

        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (users as ArrayList<User>).clear()
                for (snapshot in dataSnapshot.children) {
                    val user: User? = snapshot.getValue(User::class.java)
                    if (user != null) {
                        (users as ArrayList<User>).add(user)
                    }
                }
                userProfileAdapter = UserProfileAdapter(users!!)
                recyclerView!!.adapter = userProfileAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "searchForUsers:onCancelled", error.toException())
            }
        })
    }
}

