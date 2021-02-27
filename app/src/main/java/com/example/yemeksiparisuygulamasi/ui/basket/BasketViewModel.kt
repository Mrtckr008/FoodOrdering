package com.example.yemeksiparisuygulamasi.ui.basket

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yemeksiparisuygulamasi.domain.entity.Basket
import com.example.yemeksiparisuygulamasi.domain.entity.Food
import com.example.yemeksiparisuygulamasi.domain.entity.ResultData
import com.example.yemeksiparisuygulamasi.domain.usecase.AddFoodToBasketUseCase
import com.example.yemeksiparisuygulamasi.domain.usecase.GetAllFoodsUseCase
import com.example.yemeksiparisuygulamasi.domain.usecase.GetFoodsFromBasketUseCase
import com.example.yemeksiparisuygulamasi.domain.usecase.RemoveFoodFromBasketUseCase
import com.example.yemeksiparisuygulamasi.ui.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BasketViewModel @ViewModelInject constructor(
    private val getFoodsFromBasketUseCase: GetFoodsFromBasketUseCase,
    private val removeFoodFromBasketUseCase: RemoveFoodFromBasketUseCase
) : BaseViewModel() {
    private val _removedFoodToBasket = MutableLiveData<ResultData<Unit>>()
    val removedFoodToBasket: MutableLiveData<ResultData<Unit>>
        get() = _removedFoodToBasket

    private val _foodsListFromBasket = MutableLiveData<ResultData<List<Basket>>>()
    val foodsListFromBasket: MutableLiveData<ResultData<List<Basket>>>
        get() = _foodsListFromBasket

    fun getFoodsFromBasket(context:Context){
        viewModelScope.launch(Dispatchers.IO) {
            getFoodsFromBasketUseCase.invoke(context).collect { it ->
                handleTask(it) {
                    foodsListFromBasket.postValue(it)
                }
            }
        }
    }

    fun deleteFoodsFromBasket(context:Context, foodFromBasket: Basket){
        viewModelScope.launch(Dispatchers.IO) {
            removeFoodFromBasketUseCase.invoke(context,foodFromBasket).collect { it ->
                handleTask(it) {
                    removedFoodToBasket.postValue(it)
                }
            }
        }
    }
}