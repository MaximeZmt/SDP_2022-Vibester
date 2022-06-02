package ch.sdp.vibester.fragment

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
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
        this.gameManager =  AppPreferences.getObject<GameManager>("gameManager") as GameManager
        setGenreListeners()

        searchArtistEditable = vmGenreSetup.view.findViewById<EditText>(R.id.searchArtist).text

        setReturnBtnListener()
    }

    private fun setGenreListeners(){
        vmGenreSetup.view.findViewById<Button>(R.id.kpopButton).setOnClickListener { chooseGenre(method = LastfmMethod.BY_TAG.method, tag = "kpop", mode = R.string.kpop) }
        vmGenreSetup.view.findViewById<Button>(R.id.rockButton).setOnClickListener { chooseGenre(method = LastfmMethod.BY_TAG.method, tag = "rock", mode = R.string.rock) }
        vmGenreSetup.view.findViewById<Button>(R.id.btsButton).setOnClickListener { chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "BTS", mode = R.string.gameGenre_bts) }
        vmGenreSetup.view.findViewById<Button>(R.id.topTracksButton).setOnClickListener { chooseGenre(method = LastfmMethod.BY_CHART.method, mode = R.string.top_tracks) }
        vmGenreSetup.view.findViewById<Button>(R.id.imagDragonsButton).setOnClickListener{ chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "Imagine Dragons", mode = R.string.gameGenre_imagine_dragons) }
        vmGenreSetup.view.findViewById<Button>(R.id.billieEilishButton).setOnClickListener { chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = "Billie Eilish", mode = R.string.gameGenre_billie_eilish) }
        vmGenreSetup.view.findViewById<Button>(R.id.validateSearch).setOnClickListener{ chooseGenre(method = LastfmMethod.BY_ARTIST.method, artist = searchArtistEditable.toString(), mode = R.string.gameGenre_byArtistSearch) }
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
        findNavController().navigate(R.id.fragment_setting_setup)
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
        vmGenreSetup.view.findViewById<FloatingActionButton>(R.id.genreSetup_returnToMain).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}