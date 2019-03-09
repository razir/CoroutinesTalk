package net.appsynth.coroutinestalk.profile

interface ProfileContract {

    interface View {
        fun showError()

        fun showUserData(firstName: String?, lastName: String?)
    }

    interface Presenter {
        fun refreshWithCoroutines()

        fun refreshWithRx()

        fun clear()
    }
}