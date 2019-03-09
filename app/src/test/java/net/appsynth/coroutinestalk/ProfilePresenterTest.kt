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
import net.appsynth.coroutinestalk.models.Profile
import net.appsynth.coroutinestalk.profile.ProfileContract
import net.appsynth.coroutinestalk.profile.ProfilePresenter
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ProfilePresenterTest {

    val apiCoroutines: ProfileApiCoroutines = mock()
    val apiRx: ProfileApiRx = mock()
    val view: ProfileContract.View = mock()

    lateinit var presenter: ProfilePresenter

    @Before
    fun setup() {
        DispatchersProvider.IO = Dispatchers.Unconfined
        DispatchersProvider.Main = Dispatchers.Unconfined

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        presenter = ProfilePresenter(view, apiCoroutines, apiRx)
    }

    @After
    fun reset() {
        DispatchersProvider.reset()

        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun testRxSuccess() {
        doReturn(Single.just(Profile("Appsynth", "Asia"))).whenever(apiRx).getProfile()

        presenter.refreshWithRx()

        verify(view).showUserData("Appsynth", "Asia")
        verify(view, never()).showError()
    }

    @Test
    fun testCoroutinesSuccess() {
        whenever(runBlocking { apiCoroutines.getProfile() }).doReturn(Profile("Appsynth", "Asia"))
        presenter.refreshWithCoroutines()
        verify(view).showUserData("Appsynth", "Asia")
        verify(view, never()).showError()
    }

    @Test
    fun testRxFailure() {
        doReturn(Single.error<Profile>(IOException())).whenever(apiRx).getProfile()

        presenter.refreshWithRx()

        verify(view).showError()
    }

    @Test
    fun testCoroutinesFailure() {
        whenever(runBlocking { apiCoroutines.getProfile() }).thenAnswer { IOException() }

        presenter.refreshWithCoroutines()

        verify(view).showError()
    }
}