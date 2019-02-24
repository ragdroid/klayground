package com.ragdroid.mvvmi.core

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.fueled.mvp.core.mvp.NavigationState
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.processors.UnicastProcessor
import timber.log.Timber

abstract class MviViewModel<Action: MviAction, Result: MviResult, State: MviState>(initialState: State) : ViewModel() {

    private val stateProcessor: BehaviorProcessor<State> = BehaviorProcessor.create()
    protected val actionsProcessor: PublishProcessor<Action> = PublishProcessor.create()

    //Thanks to https://twitter.com/Hussein_Ala for the concept of UnicastProcessor
    private var navigationState: UnicastProcessor<NavigationState> = UnicastProcessor.create()

    protected var currentState: State = initialState
        private set

    open fun stateObservable(): LiveData<State> = LiveDataReactiveStreams.fromPublisher(stateProcessor)

    open fun navigationState(): LiveData<NavigationState> = LiveDataReactiveStreams.fromPublisher(navigationState)


    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val actionToResultTransformer = FlowableTransformer<Action, Result> { actions ->
        actionsToResultTransformer(actions)
    }

    /**
     * transform a stream of actions to a stream of results
     */
    abstract fun
            actionsToResultTransformer(actions: Flowable<Action>): Flowable<Result>

    /**
     * Called when the scene has been created and this
     * scene view hierarchy instantiated.
     *
     * @param data the construction arguments for this scene.
     */
    @CallSuper
    open fun processActions(actions: Flowable<Action>) {
        actions.subscribe(actionsProcessor)
        actionsProcessor
                .compose(actionToResultTransformer)
                .scan(currentState) { state, result: Result -> reduce(state, result)}
                .doOnNext {
                    Timber.d(it.toString())
                    currentState = it
                }
                .subscribe(stateProcessor)

    }

    fun onAction(action: Action) {
        actionsProcessor.onNext(action)
    }

    abstract fun reduce(previousState: State, result: Result): State

    override fun onCleared() {
        compositeDisposable.clear()
    }

    protected fun navigate(state: NavigationState) {
        navigationState.onNext(state)
    }

    protected fun Disposable.bindToLifecycle() = apply {
        compositeDisposable.add(this)
    }


}