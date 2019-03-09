package net.appsynth.coroutinestalk.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*
import net.appsynth.coroutinestalk.R
import net.appsynth.coroutinestalk.feed.ProfileFeedActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private val presenter: ProfileContract.Presenter by inject(parameters = {
        parametersOf(this)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        loadCoroutinesBtn.setOnClickListener {
            presenter.refreshWithCoroutines()
        }
        loadRxBtn.setOnClickListener {
            presenter.refreshWithRx()
        }
        openFeed.setOnClickListener {
            startActivity(ProfileFeedActivity.getStartIntent(this))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
    }

    override fun showError() {
        Toast.makeText(this, "Can't get the data", Toast.LENGTH_LONG).show()
    }

    override fun showUserData(firstName: String?, lastName: String?) {
        userNameText.text = "$firstName $lastName"
    }
}
