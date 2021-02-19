package com.example.yemeksiparisuygulamasi.data.datasource

import android.content.Context
import com.example.yemeksiparisuygulamasi.domain.entity.Food
import kotlinx.coroutines.flow.Flow

interface MenuRemoteDataSource {
    suspend fun getAllFoods(context: Context): Flow<List<Food>?>
    suspend fun searchFood(context: Context, keyWord: String): Flow<List<Food>?>
}