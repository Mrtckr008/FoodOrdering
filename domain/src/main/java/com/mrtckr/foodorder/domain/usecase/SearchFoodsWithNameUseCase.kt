package com.mrtckr.foodorder.domain.usecase

import android.content.Context
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchFoodsWithNameUseCase @Inject constructor(private val repository: MenuRepository) {
    operator fun invoke(context: Context, keyWord: String): Flow<ResultData<List<Food>>> {
        return repository.searchFood(context,keyWord)
    }
}