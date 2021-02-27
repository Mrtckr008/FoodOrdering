package com.mrtckr.foodorder.data.repository

import android.content.Context
import com.mrtckr.foodorder.data.datasource.BasketRemoteDataSource
import com.mrtckr.foodorder.domain.entity.Basket
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor(private val dataSource: BasketRemoteDataSource) :
    BasketRepository {

    override suspend fun addBasket(context: Context, food: Food, counter: Int): Flow<ResultData<Unit>> {
        return dataSource.addBasket(context, food, counter)
    }

    override suspend fun removeBasket(context: Context, foodFromBasket: Basket): Flow<ResultData<Unit>> {
        return dataSource.removeBasket(context,foodFromBasket)
    }

    override fun getBasket(context: Context): Flow<ResultData<List<Basket>>> = flow {
        emit(ResultData.Loading())
        val commentsList = dataSource.getBasket(context)
        commentsList.collect {
            if (it != null){
                emit(ResultData.Success(it))
            }
            else{
                emit(ResultData.Failed())
            }
        }
    }
}