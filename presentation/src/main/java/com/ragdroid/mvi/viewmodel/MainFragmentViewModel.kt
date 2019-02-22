package com.ragdroid.mvi.viewmodel

import androidx.lifecycle.ViewModel
import com.ragdroid.mvi.main.MainAction
import com.ragdroid.mvi.main.MainResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class MainFragmentViewModel: ViewModel() {

    val intentsSubject = PublishSubject.create<MainAction>()

    private val intentProcessor: ObservableTransformer<MainAction, MainResult> = ObservableTransformer {
        it.publish() { shared ->

        }
    }

    fun processActions(actionsObservable: Observable<MainAction>) {
        actionsObservable.subscribe(intentsSubject)
        intentsSubject
                .compose(intentProcessor)
    }


}