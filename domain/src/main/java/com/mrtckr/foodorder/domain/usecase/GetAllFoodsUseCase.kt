package com.mrtckr.foodorder.domain.usecase

import android.content.Context
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFoodsUseCase @Inject constructor(private val repository: MenuRepository) {
    operator fun invoke(context: Context): Flow<ResultData<List<Food>>> {
        return repository.getAllFood(context)
    }
}