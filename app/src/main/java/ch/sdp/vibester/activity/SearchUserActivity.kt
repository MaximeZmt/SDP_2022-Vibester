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
import ch.sdp.vibester.database.Database
import ch.sdp.vibester.profile.UserProfile
import ch.sdp.vibester.profile.UserProfileAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener




class SearchUserActivity : AppCompatActivity() {

    private var userProfileAdapter: UserProfileAdapter? = null
    private var mUsers: MutableList<UserProfile>? = null
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

        mUsers = ArrayList()

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

    private fun searchForUsers(str:String){
        val queryUsers = Database.get().getReference("users")
            .orderByChild("username")
            .startAt(str)
            .endAt(str+"\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<UserProfile>).clear()
                for (dataSnapShot in snapshot.children) {
                    val user:UserProfile? = dataSnapShot.getValue(UserProfile::class.java)
                    if (user != null) {
                        (mUsers as ArrayList<UserProfile>).add(user)
                    }
                }
                userProfileAdapter = UserProfileAdapter(mUsers!!)
                recyclerView!!.adapter = userProfileAdapter
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

