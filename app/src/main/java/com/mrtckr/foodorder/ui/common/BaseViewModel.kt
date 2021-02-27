package com.mrtckr.foodorder.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtckr.foodorder.domain.entity.ResultData

open class BaseViewModel : ViewModel() {

    val loadingErrorState = MutableLiveData<ResultData<Any>>()

    fun handleTask(task: ResultData<Any>, callback: () -> Unit = {}) {
        loadingErrorState.postValue(task)
        callback.invoke()
    }
}