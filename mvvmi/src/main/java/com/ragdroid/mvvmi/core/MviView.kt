package com.ragdroid.mvvmi.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.fueled.mvp.core.mvp.NavigationState
import io.reactivex.processors.PublishProcessor

interface MviView<Action: MviAction, State: MviState> {

    val actionsSubject: PublishProcessor<Action>
    val viewModel: MviViewModel<Action,*,State>

    fun subscribeToViewState() {
        viewModel.stateLiveData()
                .observe(lifecycleOwner, Observer(this::render))
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