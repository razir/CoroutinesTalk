package net.appsynth.coroutinestalk.di

import net.appsynth.coroutinestalk.api.buildApiCoroutines
import net.appsynth.coroutinestalk.api.buildApiRx
import net.appsynth.coroutinestalk.feed.ProfileFeedContract
import net.appsynth.coroutinestalk.feed.ProfileFeedPresenter
import net.appsynth.coroutinestalk.profile.ProfileContract
import net.appsynth.coroutinestalk.profile.ProfilePresenter
import org.koin.dsl.module.module

val koinModule = module {
    factory<ProfileContract.Presenter> { (view: ProfileContract.View) ->
        ProfilePresenter(view = view, profileApiCoroutines = get(), profileApiRx = get())
    }

    factory<ProfileFeedContract.Presenter> { (view: ProfileFeedContract.View) ->
        ProfileFeedPresenter(view = view, profileApiCoroutines = get(), profileApiRx = get())
    }

    single { buildApiCoroutines() }

    single { buildApiRx() }
}