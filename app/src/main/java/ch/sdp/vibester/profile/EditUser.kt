package ch.sdp.vibester.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.sdp.vibester.R

class EditUser : AppCompatActivity() {

    private var popupTitle = ""
    private var popupText = ""
    private var popupButton = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_edit_user)
    }
}