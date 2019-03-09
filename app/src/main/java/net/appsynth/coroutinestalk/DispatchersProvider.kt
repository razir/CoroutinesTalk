package net.appsynth.coroutinestalk

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object DispatchersProvider {
    var Main: CoroutineContext = Dispatchers.Main
    var IO: CoroutineContext = Dispatchers.IO

    fun reset() {
        Main = Dispatchers.Main
        IO = Dispatchers.IO
    }
}