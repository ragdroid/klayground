package com.ragdroid.mvvmi.core

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.reactivex.Flowable

/**
 * `MviView<MviAction, MviState>`: Firstly, we need to provide the information about our `MviAction` and `MviState` so
 * we pass them as the generic parameters. `class AddLogoFragment : MviView<LogoAction, LogoViewState>`
 */
interface MviView<Action: MviAction, State: MviState> {

    /**
     * It expects Android's arch-comp `ViewModel` instance to be provided by us.
     */
    val viewModel: MviViewModel<Action, *, State>

    fun subscribeToViewState() {
        viewModel.stateLiveData()
                .observe(lifecycleOwner, Observer(this::render))
    }

    /**
     *  For implementing an `MviView`, we need to provide the implementation of a `lifecycleOwner`. It's used to
     *  subscribe to `LiveData` events and hooking 'em up to the given `lifecycleOwner` to automatically unsubscribe.
     */
    val lifecycleOwner: LifecycleOwner
    fun navigationLifecycleOwner(): LifecycleOwner = lifecycleOwner

    private fun subscribeToNavigationState() {
        viewModel.navigationStateLiveData()
                .observe(lifecycleOwner, Observer {
                    it.getContentIfNotHandled()?.let {
                        navigate(it)
                    }
                })
    }

    /**
     * Here we get our `ViewState` as a parameter passed to it. We can then use this function to update the state of
     * our Views. Upon rotation, LiveData will emit previously cached value of `state`.
     */
    fun render(state: State)

    /**
     * Here `NavigationState` is passed as a parameter and we can perform coressponding Navigation like showing a
     * dialog, opening another Fragment etc. Note that in the case of rotation, this event is not passed again to the
     * view to avoid navigating twice.
     */
    fun navigate(navigationState: NavigationState)

    /**
     * Don't forget to call `super.onMviCreated()` after `viewModel` has been initialzed. We can call it inside
     * `onViewCreated()` of `Fragment` or inside `onCreate()` for an `Activity`. By doing this,
     * our `View` gets subscribed automatically to the `state` and `navigation` events.
     */
    fun onMviViewCreated(savedInstanceState: Bundle?) {
        subscribeToViewState()
        subscribeToNavigationState()
        if (savedInstanceState == null) {
            viewModel.processActions(provideActions())
        }
    }

    /**
     * For passing actions to the `ViewModel`, we get a method `onAction(MviAction)` that we can call directly inside
     * our View class or `viewModel.onAction()`.
     */
    fun onAction(action: Action) {
        viewModel.onAction(action)
    }

    fun provideActions(): Flowable<Action> = Flowable.empty()
}