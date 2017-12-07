package com.ragdroid.mvi.helpers

import com.ragdroid.data.base.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by garimajain on 19/11/17.
 */
class SchedulersProviderImpl: SchedulerProvider {
    override fun computation(): Scheduler {
        return Schedulers.computation();
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}