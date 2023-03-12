package com.zugazagoitia.spotifystalker

import android.app.Application
import com.google.android.material.color.DynamicColors

class SpotStalkApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
