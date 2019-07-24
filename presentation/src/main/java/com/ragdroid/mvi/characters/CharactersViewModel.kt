package com.ragdroid.mvi.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ragdroid.data.MainRepository
import com.ragdroid.mvi.main.MainViewState
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
        private val mainRepository: MainRepository
) {

    fun stateLiveData(): LiveData<MainViewState> = stateLiveData
    private val stateLiveData = MutableLiveData<MainViewState>()

}