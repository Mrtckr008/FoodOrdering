package com.mrtckr.foodorder.domain.repository

import android.content.Context
import com.mrtckr.foodorder.domain.entity.Basket
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow

interface BasketRepository {
    suspend fun addBasket(context: Context, food: Food, counter: Int): Flow<ResultData<Unit>>
    suspend fun removeBasket(context: Context, foodFromBasket: Basket): Flow<ResultData<Unit>>
    suspend fun getBasket(context: Context):  Flow<ResultData<List<Basket>>>
}