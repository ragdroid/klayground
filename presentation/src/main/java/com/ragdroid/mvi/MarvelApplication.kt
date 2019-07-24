package com.ragdroid.mvi

import androidx.databinding.DataBindingUtil
import com.ragdroid.mvi.dagger.DaggerAppComponent
import com.ragdroid.mvi.helpers.MarvelBindingComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by garimajain on 18/11/17.
 */
class MarvelApplication : DaggerApplication() {

    @Inject lateinit var bindingComponent: MarvelBindingComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }


    override fun onCreate() {
        super.onCreate()
        plantTimber()
        attachLeakCanary()
        DataBindingUtil.setDefaultComponent(bindingComponent)
    }


    private fun attachLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
            Timber.d("Planted Timber tree")
        }
    }
}