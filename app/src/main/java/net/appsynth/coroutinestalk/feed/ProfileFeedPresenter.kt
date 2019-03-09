package net.appsynth.coroutinestalk.feed

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import net.appsynth.coroutinestalk.api.ProfileApiCoroutines
import net.appsynth.coroutinestalk.api.ProfileApiRx
import net.appsynth.coroutinestalk.extensions.addTo
import net.appsynth.coroutinestalk.models.Profile
import net.appsynth.coroutinestalk.models.UserActivity
import kotlin.coroutines.CoroutineContext

class ProfileFeedPresenter(
    private var view: ProfileFeedContract.View?,
    private val profileApiCoroutines: ProfileApiCoroutines,
    private val profileApiRx: ProfileApiRx
) : ProfileFeedContract.Presenter, CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main


    override fun refreshWithCoroutines() {
        refreshWithCoroutinesConcat()
        //refreshWithCoroutinesFlatMap()
    }

    override fun refreshWithRx() {
        refreshWithRxConcat()
//        refreshWithRxFlatMap()
    }

    private fun refreshWithCoroutinesConcat() {
        launch {
            try {
                val profileDeferred = async(Dispatchers.IO) {
                    profileApiCoroutines.getProfile()
                }
                val feedDeferred = async(Dispatchers.IO) {
                    profileApiCoroutines.getUserActivity()
                }
                val profileData = profileDeferred.await()
                val feedList = feedDeferred.await()

                view?.showUserData(profileData.firstName, profileData.lastName)
                view?.showUserFeed(getFeedText(feedList))
            } catch (e: Exception) {
                view?.showError()
            }
        }
    }

    /**
     * Implement RxJava flatMap behavior using coroutines here
     * You can find Rx implementation below [refreshWithRxFlatMap]
     * Tip: The previous example can give you some ideas
     * [net.appsynth.coroutinestalk.profile.ProfilePresenter.refreshWithCoroutines]
     */
    private fun refreshWithCoroutinesFlatMap() {

    }

    private fun refreshWithRxConcat() {
        class UserDataWrapper(val profile: Profile, val feed: List<UserActivity>)

        Single.zip(
            profileApiRx.getProfile(),
            profileApiRx.getUserActivity(),
            BiFunction<Profile, List<UserActivity>, UserDataWrapper> { profile, feed ->
                UserDataWrapper(profile, feed)
            }
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ profileDataWrapper ->
                val profileData = profileDataWrapper.profile
                val feed = profileDataWrapper.feed
                view?.showUserData(profileData.firstName, profileData.lastName)
                view?.showUserFeed(getFeedText(feed))
            }, {
                view?.showError()
            }).addTo(compositeDisposable)
    }

    private fun refreshWithRxFlatMap() {
        profileApiRx.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                view?.showUserData(it.firstName, it.lastName)
            }.observeOn(Schedulers.io())
            .flatMap {
                profileApiRx.getUserActivity()
            }.subscribe({
                view?.showUserFeed(getFeedText(it!!))
            }, {
                view?.showError()
            })
            .addTo(compositeDisposable)
    }

    override fun clear() {
        //Cancel coroutines tasks
        job.cancel()

        //Cancel rx tasks
        compositeDisposable.clear()
        view = null
    }

    private fun getFeedText(list: List<UserActivity>): String {
        return list.joinToString(separator = "\n") {
            "${it.date}: ${it.type}"
        }
    }

    private val compositeDisposable = CompositeDisposable()
}