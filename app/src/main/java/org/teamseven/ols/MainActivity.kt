package org.teamseven.ols

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setContentView(R.layout.activity_main)
    }
}