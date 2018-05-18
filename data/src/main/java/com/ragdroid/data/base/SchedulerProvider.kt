package com.ragdroid.data.base

import io.reactivex.Scheduler

/**
 * Created by garimajain on 18/11/17.
 */
interface SchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}