package ch.sdp.vibester.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.sdp.vibester.R
import ch.sdp.vibester.activity.QrScanningActivity
import ch.sdp.vibester.activity.profile.PublicProfileActivity
import ch.sdp.vibester.auth.FireBaseAuthenticator
import ch.sdp.vibester.database.DataGetter
import ch.sdp.vibester.database.ImageGetter
import ch.sdp.vibester.helper.Helper
import ch.sdp.vibester.user.OnItemClickListener
import ch.sdp.vibester.user.User

import ch.sdp.vibester.user.UserProfileAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Search for users based on their usernames.
 */
@AndroidEntryPoint
class SearchUserFragment : Fragment(), OnItemClickListener {

    @Inject
    lateinit var usersRepo: DataGetter

    @Inject
    lateinit var imageGetter: ImageGetter

    @Inject
    lateinit var authenticator: FireBaseAuthenticator

    var users = arrayListOf<User>()
    lateinit var userProfileAdapter: UserProfileAdapter

    private var searchEditText: EditText? = null

    private var uidList: ArrayList<String> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView(view)

        searchEditText = view.findViewById(R.id.searchUserET)
        searchForUsers("")

        setScanBtnListener(view)

        setSearchFieldListener()
    }

    private fun setScanBtnListener(view: View) {
        val buttonScan: FloatingActionButton = view.findViewById(R.id.searchUser_scanning)

        buttonScan.setOnClickListener {
            val qrIntent = Intent(requireActivity(), QrScanningActivity::class.java)
            qrIntent.putExtra("uidList", uidList)
            startActivity(qrIntent)
        }
    }

    private fun setSearchFieldListener() {
        searchEditText!!.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchForUsers(p0.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    private fun setUpRecycleView(view: View) {
        view.findViewById<RecyclerView>(R.id.searchList).apply {
            layoutManager = LinearLayoutManager(context)
            userProfileAdapter = UserProfileAdapter(users, authenticator, usersRepo, imageGetter, this@SearchUserFragment)
            adapter = userProfileAdapter
            setHasFixedSize(true)
        }
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
     * @param inputUsername search text inputted by user
     */
    private fun searchForUsers(inputUsername:String){
        usersRepo.searchByField("username", inputUsername, callback = ::setUserInAdapter, callbackUid = ::setUserInAdapter2)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(requireActivity(), PublicProfileActivity::class.java)
        intent.putExtra("UserId", users[position].uid)
        intent.putExtra("ScoresOrFollowing", R.string.profile_following.toString())
        startActivity(intent)
    }
}


