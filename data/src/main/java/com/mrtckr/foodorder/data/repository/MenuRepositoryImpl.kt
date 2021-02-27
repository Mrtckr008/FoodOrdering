package com.mrtckr.foodorder.data.repository

import android.content.Context
import com.mrtckr.foodorder.data.datasource.MenuRemoteDataSource
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(private val dataSource: MenuRemoteDataSource) :
    MenuRepository {
    override fun getAllFood(context: Context): Flow<ResultData<List<Food>>> = flow {
        emit(ResultData.Loading())
        val commentsList = dataSource.getAllFoods(context)
        commentsList.collect {
            if (it != null){
                emit(ResultData.Success(it))
            }
            else{
                emit(ResultData.Failed())
            }
        }
    }

    override fun searchFood(context: Context, keyWord: String): Flow<ResultData<List<Food>>>  = flow {
        emit(ResultData.Loading())
        val commentsList = dataSource.searchFood(context, keyWord)
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