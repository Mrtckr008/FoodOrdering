package com.example.yemeksiparisuygulamasi.ui.menu

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yemeksiparisuygulamasi.domain.entity.Food
import com.example.yemeksiparisuygulamasi.domain.entity.ResultData
import com.example.yemeksiparisuygulamasi.domain.usecase.AddFoodToBasketUseCase
import com.example.yemeksiparisuygulamasi.domain.usecase.GetAllFoodsUseCase
import com.example.yemeksiparisuygulamasi.domain.usecase.SearchFoodsWithNameUseCase
import com.example.yemeksiparisuygulamasi.ui.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.collect

class MenuViewModel @ViewModelInject constructor(
    private val getAllFoodsUseCase: GetAllFoodsUseCase,
    private val searchFoodsWithNameUseCase: SearchFoodsWithNameUseCase,
    private val addFoodToBasketUseCase: AddFoodToBasketUseCase,
) : BaseViewModel() {

    private val _foodList = MutableLiveData<ResultData<List<Food>>>()
    val foodList: MutableLiveData<ResultData<List<Food>>>
        get() = _foodList

    private val _searchedFoodList = MutableLiveData<ResultData<List<Food>>>()
    val searchedFoodList: MutableLiveData<ResultData<List<Food>>>
        get() = _searchedFoodList

    private val _addedFoodToBasket = MutableLiveData<ResultData<Unit>>()
    val addedFoodToBasket: MutableLiveData<ResultData<Unit>>
        get() = _addedFoodToBasket

    fun getAllFoods(context:Context){
        viewModelScope.launch(Dispatchers.IO) {
            getAllFoodsUseCase.invoke(context).collect { it ->
                handleTask(it) {
                    foodList.postValue(it)
                }
            }
        }
    }

    fun searchFoodsWithKeyword(context:Context, keyword: String){
        viewModelScope.launch(Dispatchers.IO) {
            searchFoodsWithNameUseCase.invoke(context, keyword).collect { it ->
                handleTask(it) {
                    searchedFoodList.postValue(it)
                }
            }
        }
    }

    fun addFoodsToBasket(context:Context, food: Food, counter: Int){
        viewModelScope.launch(Dispatchers.IO) {
            addFoodToBasketUseCase.invoke(context, food, counter).collect { it ->
                handleTask(it) {
                    addedFoodToBasket.postValue(it)
                }
            }
        }
    }
}