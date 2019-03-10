package net.appsynth.coroutinestalk

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.appsynth.coroutinestalk.api.ProfileApiCoroutines
import net.appsynth.coroutinestalk.api.ProfileApiRx
import net.appsynth.coroutinestalk.feed.ProfileFeedContract
import net.appsynth.coroutinestalk.feed.ProfileFeedPresenter
import net.appsynth.coroutinestalk.models.Profile
import net.appsynth.coroutinestalk.models.UserActivity
import net.appsynth.coroutinestalk.profile.ProfileContract
import net.appsynth.coroutinestalk.profile.ProfilePresenter
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ProfileFeedPresenterTest {

    val apiCoroutines: ProfileApiCoroutines = mock()
    val apiRx: ProfileApiRx = mock()
    val view: ProfileFeedContract.View = mock()

    lateinit var presenter: ProfileFeedPresenter

    @Before
    fun setup() {
        DispatchersProvider.IO = Dispatchers.Unconfined
        DispatchersProvider.Main = Dispatchers.Unconfined

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        presenter = ProfileFeedPresenter(view, apiCoroutines, apiRx)
    }

    @After
    fun reset() {
        DispatchersProvider.reset()

        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun testRxSuccess() {
        val profile = Profile("Appsynth", "Asia")
        val feed = listOf(UserActivity("running", "today"), UserActivity("sleep", "last year"))
        doReturn(Single.just(profile)).whenever(apiRx).getProfile()
        doReturn(Single.just(feed)).whenever(apiRx).getUserActivity()

        presenter.refreshWithRx()

        verify(view).showUserData("Appsynth", "Asia")
        verify(view).showUserFeed("today: running\nlast year: sleep")
        verify(view, never()).showError()
    }

    /**
     * Implement tests for success case here
     * You can use [ProfilePresenterTest.testCoroutinesSuccess] for the reference
     */
    @Test
    fun testCoroutinesSuccess() {

    }

    @Test
    fun testRxFailure() {
        val profile = Profile("Appsynth", "Asia")
        doReturn(Single.just(profile)).whenever(apiRx).getProfile()
        doReturn(Single.error<List<UserActivity>>(IOException())).whenever(apiRx).getUserActivity()

        presenter.refreshWithRx()

        verify(view).showError()
    }

    /**
     * Implement tests for success case here
     * You can use [ProfilePresenterTest.testCoroutinesFailure] for the reference
     */
    @Test
    fun testCoroutinesFailure() {

    }
}