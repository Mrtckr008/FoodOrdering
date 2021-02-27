package com.mrtckr.foodorder.domain.repository

import android.content.Context
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getAllFood(context: Context): Flow<ResultData<List<Food>>>
    fun searchFood(context: Context, keyWord: String): Flow<ResultData<List<Food>>>
}