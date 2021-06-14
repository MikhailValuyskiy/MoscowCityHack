package ru.androidheroes.starthubapp

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import ru.androidheroes.starthubapp.ui.feed.NotificationHelper
import timber.log.Timber

class MoscowHackatonApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDebugTools()

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "Московский инновационный кластер."
        )
    }

    private fun initDebugTools() {
        if (BuildConfig.DEBUG) {
            initTimber()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        var instance: MoscowHackatonApp? = null
            private set
    }
}
