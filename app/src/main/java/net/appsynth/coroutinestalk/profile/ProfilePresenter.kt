package net.appsynth.coroutinestalk.profile

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import net.appsynth.coroutinestalk.api.ProfileApiCoroutines
import net.appsynth.coroutinestalk.api.ProfileApiRx
import net.appsynth.coroutinestalk.extensions.addTo
import kotlin.coroutines.CoroutineContext

class ProfilePresenter(
    private var view: ProfileContract.View?,
    private val profileApiCoroutines: ProfileApiCoroutines,
    private val profileApiRx: ProfileApiRx
) : ProfileContract.Presenter, CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main


    override fun refreshWithCoroutines() {
        launch {
            try {
                val profileData = withContext(Dispatchers.IO) {
                    profileApiCoroutines.getProfile()
                }
                view?.showUserData(profileData.firstName, profileData.lastName)
            } catch (e: Exception) {
                view?.showError()
            }
        }
    }

    override fun refreshWithRx() {
        profileApiRx.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ profileData ->

                view?.showUserData(profileData.firstName, profileData.lastName)

            }, {
                view?.showError()
            }).addTo(compositeDisposable)
    }

    override fun clear() {
        //Cancel coroutines tasks
        job.cancel()

        //Cancel rx tasks
        compositeDisposable.clear()
        view = null
    }

    private val compositeDisposable = CompositeDisposable()
}