package ch.sdp.vibester.fragment

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ch.sdp.vibester.R
import ch.sdp.vibester.api.LastfmApiInterface
import ch.sdp.vibester.api.LastfmMethod
import ch.sdp.vibester.api.LastfmUri
import ch.sdp.vibester.database.AppPreferences
import ch.sdp.vibester.helper.GameManager
import ch.sdp.vibester.helper.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreSetupFragment: Fragment(R.layout.fragment_layout_genre) {

    private var searchArtistEditable: Editable? = null
    lateinit var gameManager: GameManager
    // TODO: OFFLINE
    private var vmGenreSetup = ViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vmGenreSetup.view = view
        vmGenreSetup.ctx = view.context

        setGenreListeners()

        val bundle = this.arguments
        if (bundle != null) {
            gameManager = bundle.get("gameManager") as GameManager
        }

        searchArtistEditable = vmGenreSetup.view.findViewById<EditText>(R.id.searchArtist).text

        setReturnBtnListener()
    }

    private fun setGenreListeners(){
        val kpop = vmGenreSetup.view.findViewById<Button>(R.id.kpopButton)
        val rock = vmGenreSetup.view.findViewById<Button>(R.id.rockButton)
        val bts = vmGenreSetup.view.findViewById<Button>(R.id.btsButton)
        val topTracks = vmGenreSetup.view.findViewById<Button>(R.id.topTracksButton)
        val imagDragons = vmGenreSetup.view.findViewById<Button>(R.id.imagDragonsButton)
        val billieEilish = vmGenreSetup.view.findViewById<Button>(R.id.billieEilishButton)
        val validate = vmGenreSetup.view.findViewById<Button>(R.id.validateSearch)


        kpop.setOnClickListener { chooseGenreByTag("kpop", R.string.kpop) }
        rock.setOnClickListener { chooseGenreByTag("rock", R.string.rock) }
        bts.setOnClickListener { chooseGenreByArtist("BTS", R.string.gameGenre_bts) }
        topTracks.setOnClickListener { chooseGenre(method = LastfmMethod.BY_CHART.method, mode = R.string.top_tracks) }
        imagDragons.setOnClickListener{ chooseGenreByArtist("Imagine Dragons", R.string.gameGenre_imagine_dragons) }
        billieEilish.setOnClickListener { chooseGenreByArtist("Billie Eilish", R.string.gameGenre_billie_eilish) }
        validate.setOnClickListener{ chooseGenreByArtist(searchArtistEditable.toString(), R.string.gameGenre_byArtistSearch) }
    }

    private fun chooseGenreByTag(tag: String, mode: Int) {
        chooseGenre(method = LastfmMethod.BY_TAG.method, tag = tag, mode = mode)
    }

    private fun chooseGenreByArtist(artist: String, mode: Int) {
        chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = artist, mode = mode)
    }


    /**
     * Set game genre. Fetch the data from Lastfm.
     * @param method: lastfm method to fetch songs: BY_ARTIST, BY_TAG
     * @param artist: artist to fetch songs from; used in BY_ARTIST method
     * @param tag: tag (genre) to fetch songs from: used in BY_TAG method
     * @param mode: official game mode name
     */
    private fun chooseGenre(method: String = "", artist: String = "", tag: String = "", mode: Int = 0, playOffline: Boolean = false) {

        val uri = LastfmUri()

        uri.method = method
        uri.artist = artist
        uri.tag = tag

        AppPreferences.setStr("gameMode", getString(mode))
        gameManager.gameMode = getString(mode)
        setGameSongList(uri, playOffline)

        val bundle = bundleOf("gameManager" to gameManager)
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.main_bottom_nav_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.fragment_setting_setup, bundle)
    }


    /**
     * Fetch data from Lastfm and set song list in a GameManager
     * @param uri: contains all Lastfm query parameters (method, artist, tag)
     */
    private fun setGameSongList(uri: LastfmUri, playOffline: Boolean = false) {
        if (playOffline) {
            val external = vmGenreSetup.ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            if (external != null) {
                gameManager.setOffline(external, !playOffline)
                gameManager.setGameSongList(Gson().toJson(""), uri.method)
            }
        } else {
            val service = LastfmApiInterface.createLastfmService()
            val call = service.getSongList(uri.convertToHashmap())
            call.enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable?) {}
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    gameManager.setGameSongList(Gson().toJson(response.body()), uri.method)
                }
            })
        }
    }

    private fun setReturnBtnListener() {
        val returnToMain = vmGenreSetup.view.findViewById<FloatingActionButton>(R.id.genreSetup_returnToMain)
        returnToMain.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}