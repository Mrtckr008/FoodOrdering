package com.example.yemeksiparisuygulamasi.data.repository

import android.content.Context
import com.example.yemeksiparisuygulamasi.data.datasource.BasketRemoteDataSource
import com.example.yemeksiparisuygulamasi.data.datasource.MenuRemoteDataSource
import com.example.yemeksiparisuygulamasi.domain.entity.Basket
import com.example.yemeksiparisuygulamasi.domain.entity.Food
import com.example.yemeksiparisuygulamasi.domain.entity.ResultData
import com.example.yemeksiparisuygulamasi.domain.repository.BasketRepository
import com.example.yemeksiparisuygulamasi.domain.repository.MenuRepository
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