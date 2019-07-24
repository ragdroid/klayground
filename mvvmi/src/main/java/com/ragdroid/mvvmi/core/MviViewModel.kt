package com.ragdroid.mvvmi.core

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ragdroid.mvvmi.core.helper.Event
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import timber.log.Timber

/**
 * we first need to *extend* `MviViewModel<MviAction, MviResult, MviState>` and provide it with the information about
 * our `MviAction`, `MviResult` and `MviState`. We also pass an initial state to the `MviViewModel(initialState)`
 * constructor. `class AddLogoViewModel() : MviViewModel<LogoAction, LogoResult, LogoViewState>(LogoViewState.init())`.
 */
abstract class MviViewModel<Action: MviAction, Result: MviResult, State: MviState>(initialState: State) : ViewModel() {

    private val stateProcessor: PublishProcessor<State> = PublishProcessor.create()
    protected val actionsProcessor: PublishProcessor<Action> = PublishProcessor.create()

    protected var currentState: State = initialState
        private set

    /**
     * not using LiveDataReactiveStreams here as when posting events from a different fragment, events were not reaching
     * render because LiveDataSubscribers become inactive. So, we will instead post ourselves
     */
    open fun stateLiveData(): LiveData<State> = stateLiveData
    private val stateLiveData = MutableLiveData<State>()

    open fun stateFlowable(): Flowable<State> = stateProcessor

    open fun navigationStateLiveData(): LiveData<Event<NavigationState>> = navigationLiveData
    private var navigationLiveData = MutableLiveData<Event<NavigationState>>()


    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val actionToResultTransformer = FlowableTransformer<Action, Result> { actions ->
        actionsToResultTransformer(actions)
    }

    /**
     * This function is passed a stream of actions. We can choose whichever Rx operator we would like to use to convert
     * each action into (zero to multiple) results.
     * With zero to multiple results, I mean those actions which produce
     * purely side-effects. It would be performant to not emit any result in this case (`Flowable.empty()`) to avoid
     * re-rendering of a state.
     */
    abstract fun
            actionsToResultTransformer(actions: Flowable<Action>): Flowable<Result>

    @CallSuper
    fun processActions(actions: Flowable<Action>) {
        actionsProcessor.mergeWith(actions)
                .doOnNext {
                    Timber.v("onAction $it")
                }
                .compose(actionToResultTransformer)
                .doOnNext {
                    Timber.v("onResult $it")
                }
                .scan(currentState) { state, result: Result -> reduce(state, result)}
                .doOnNext {
                    Timber.v(it.toString())
                    currentState = it
                    stateLiveData.postValue(it)
                }
                .doOnSubscribe { Timber.d("subscribed to states") }
                .subscribe(stateProcessor::onNext, Timber::e)
                .bindToLifecycle()
    }

    /**
     * Inside our `actionToResultTransformer()`, we can emit a side-effect `MviAction` as well by using
     * `onAction(MviAction)`. This action will then again go into our MVI cycle and will be processed.
     */
    fun onAction(action: Action) {
        actionsProcessor.onNext(action)
    }


    /**
     * This is the only place where we should make any changes to the state of the screen.
     */
    abstract fun reduce(previousState: State, result: Result): State

    override fun onCleared() {
        compositeDisposable.clear()
    }

    /**
     * At any point, generally inside `actionToResultTransformer()` we can call `navigate` to perform navigation
     * side-effects.
     */
    protected fun navigate(state: NavigationState) {
        navigationLiveData.postValue(Event(state))
    }

    /**
     * to easily dispose off our subscriptions.
     */
    protected fun Disposable.bindToLifecycle() = apply {
        compositeDisposable.add(this)
    }


}