package net.appsynth.coroutinestalk

import android.app.Application
import net.appsynth.coroutinestalk.di.koinModule
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(koinModule))
    }
}