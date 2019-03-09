package net.appsynth.coroutinestalk.feed

interface ProfileFeedContract {

    interface View {
        fun showError()

        fun showUserData(firstName: String?, lastName: String?)

        fun showUserFeed(feed: String?)
    }

    interface Presenter {
        fun refreshWithCoroutines()

        fun refreshWithRx()

        fun clear()
    }
}