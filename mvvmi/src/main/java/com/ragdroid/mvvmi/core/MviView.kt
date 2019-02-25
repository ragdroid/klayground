package com.ragdroid.mvvmi.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.fueled.mvp.core.mvp.NavigationState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import timber.log.Timber

interface MviView<Action: MviAction, State: MviState> {

    val actionsSubject: PublishProcessor<Action>
    val viewModel: MviViewModel<Action,*,State>

    fun subscribeToViewState() {
        viewModel.stateLiveData()
                .observe(lifecycleOwner, Observer(this::render))
//        viewModel.stateFlowable()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    render(it)
//                    Timber.d(it.toString())
//                }
    }

    val lifecycleOwner: LifecycleOwner
    fun navigationLifecycleOwner(): LifecycleOwner = lifecycleOwner

    private fun subscribeToNavigationState() {
        viewModel.navigationStateLiveData()
                .observe(navigationLifecycleOwner(), Observer(this::navigate))
    }

    fun render(state: State)
    fun navigate(navigationState: NavigationState)

    fun onMviViewCreated() {
        subscribeToViewState()
        subscribeToNavigationState()
    }

    fun onAction(action: Action) {
        actionsSubject.onNext(action)
    }

}