# Base MVI
This module contains following main classes:

 - [`MviView`](https://github.com/ragdroid/klayground/blob/master/mvvmi/src/main/java/com/ragdroid/mvvmi/core/MviView.kt) : Base Interface that a `Fragment` or `Activity` needs to implement which provides us with some default MVI functionality.
 - [`MviViewModel`](https://github.com/ragdroid/klayground/blob/master/mvvmi/src/main/java/com/ragdroid/mvvmi/core/MviViewModel.kt) : `abstract` class needs to be extended by a `ViewModel` which provides us with base MVI functionality and abstracts away the boilerplate code.
 - [`MviState`](https://github.com/ragdroid/klayground/blob/master/mvvmi/src/main/java/com/ragdroid/mvvmi/core/MviState.kt) : Interface for all the states of View.
 - [`MviAction`](https://github.com/ragdroid/klayground/blob/master/mvvmi/src/main/java/com/ragdroid/mvvmi/core/MviAction.kt) : Interface for all View Actions
 - [`MviResult`](https://github.com/ragdroid/klayground/blob/master/mvvmi/src/main/java/com/ragdroid/mvvmi/core/MviResult.kt) : Interface for all Results
 - [`NavigationState`](https://github.com/ragdroid/klayground/blob/master/mvvmi/src/main/java/com/ragdroid/mvvmi/core/NavigationState.kt) : Interface for all Navigation for an `Activity` or `Fragment`.

 
## Creating MVI Flow
Let's take an example of Upload Logo to list down all the steps and point to it's classes.

### ViewState
 - Start by creating a [`MainViewState`](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/main/MainViewState.kt) class inside your presentation module. Make it Implement `MviState`
 - You can also add any [`MainAction`](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/main/MainViewState.kt) within this file implementing `MviAction` and similarly [`MainResult`](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/main/MainViewState.kt) implementing `MviResult`.
 
### View
Next on the View side. Let's create our [`MainFragment`](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/main/MainFragment.kt) implementing `MviView`.

 - `MviView<MviAction, MviState>`: Firstly, we need to provide the information about our `MviAction` and `MviState` so we pass them as the generic parameters. `class AddLogoFragment : MviView<LogoAction, LogoViewState>`
 - **lifecycleOwner** : For implementing an `MviView`, we need to provide the implementation of a `lifecycleOwner`. It's used to subscribe to `LiveData` events and hooking 'em up to the given `lifecycleOwner` to automatically unsubscribe.
 - **viewModel** : It expects Android's arch-comp `ViewModel` instance to be provided by us.
 - **super.onMviViewCreated(savedInstanceState)**: **IMPORTANT** Don't forget to call `super.onMviCreated()` after `viewModel` has been initialzed. We can call it inside `onViewCreated()` of `Fragment` or inside `onCreate()` for an `Activity`. By doing this, our `View` gets subscribed automatically to the `state` and `navigation` events.
 - **render()**: Next, we implement our `render(ViewState)` function. Here we get our `ViewState` as a parameter passed to it. We can then use this function to update the state of our Views. Upon rotation, LiveData will emit previously cached value of `state`.
 - **navigate**: We then implement our `navigate(NavigationState)` function. Here `NavigationState` is passed as a parameter and we can perform coressponding Navigation like showing a dialog, opening another Fragment etc. Note that in the case of rotation, this event is not passed again to the view to avoid navigating twice.
 - **actions**: For passing actions to the `ViewModel`, we get a method `onAction(MviAction)` that we can call directly inside our View class or `viewModel.onAction()`.
 - **Manage Subscription**: If we wish to manage subscription of events yourself, we can skip calling `super.onMviCreated()` and instead subscribe to `state` and `navigation` by subscribing to `viewModel.stateLiveData()` or `viewModel.stateFlowable()`. For navigation events, we can subscribe to `viewModel.navigationStateLiveData()`.
 
### ViewModel
Let's look at the implementation of a `ViewModel`, le [`MainFragmentViewModel`](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/viewmodel/MainFragmentViewModel.kt).

 - Like View, we first need to *extend* `MviViewModel<MviAction, MviResult, MviState>` and provide it with the information about our `MviAction`, `MviResult` and `MviState`. We also pass an initial state to the `MviViewModel(initialState)` constructor. `class AddLogoViewModel() : MviViewModel<LogoAction, LogoResult, LogoViewState>(LogoViewState.init())`.
 - **Default Actions**: We can optionally emit some default actions to the Mvi Flow inside the `init{ }` block as shown in [`MainFragmentViewModel`](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/viewmodel/MainFragmentViewModel.kt).
 - **actionToResultTransformer(actions)**: We then implement the `actionToResultTransformer()` function. This function is passed a stream of actions. We can choose whichever Rx operator we would like to use to convert each action into (zero to multiple) results. See [`MainFragmentViewModel`](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/viewmodel/MainFragmentViewModel.kt) for a `publish` and `merge()` implementation. When I say zero to multiple results, I mean those actions which produce purely side-effects. It would be performant to not emit any result in this case (`Flowable.empty()`) to avoid re-rendering of a state.
 - **navigate(navigationState**): At any point, generally inside `actionToResultTransformer()` we can call `navigate` to perform navigation side-effects.
 - **Side-effect**: Inside our `actionToResultTransformer()`, we can emit a side-effect `MviAction` as well by using `onAction(MviAction)`. This action will then again go into our MVI cycle and will be processed.
 - **reduce(previousState, result)**: We then implement our `reducer()` function which takes in `previousState` and `result` as input and outputs the next state. This is the only place where we should make any changes to the state of the screen. I generally like to keep the reducer function inside `ViewState` object so that I can call `previousState.reduce(result)` and our `ViewModel` stays clean. Ex: [MainViewState.reducer()](https://github.com/ragdroid/klayground/blob/master/presentation/src/main/java/com/ragdroid/mvi/main/MainViewState.kt).
 - **currentState**: At any point if we need access to the `currentState`, we can get that inside our `ViewModel` but the setter is always private. 
 - **bindToLifecycle()**: MviViewModel also provides us with a `Disposable` helper function to easily dispose off our subscriptions. Example, See [`MviViewModel`](https://github.com/ragdroid/klayground/blob/master/mvvmi/src/main/java/com/ragdroid/mvvmi/core/MviViewModel.kt). With MVI, we shouldn't ever have to subscribe to anything ourselves, as everything becomes part of the chain. Subscribing to states is already handled inside `MviViewModel` for us. 
 
 