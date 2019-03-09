package net.appsynth.coroutinestalk.feed

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile_feed.*
import net.appsynth.coroutinestalk.R
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProfileFeedActivity : AppCompatActivity(), ProfileFeedContract.View {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ProfileFeedActivity::class.java)
        }
    }

    private val presenter: ProfileFeedContract.Presenter by inject(parameters = {
        parametersOf(this)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_feed)
        loadCoroutinesBtn.setOnClickListener {
            presenter.refreshWithCoroutines()
        }
        loadRxBtn.setOnClickListener {
            presenter.refreshWithRx()
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

    override fun showUserFeed(feed: String?) {
        userFeed.text = feed
    }
}
