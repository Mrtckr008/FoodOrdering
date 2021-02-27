package com.mrtckr.foodorder.domain.usecase

import android.content.Context
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.domain.repository.BasketRepository
import com.mrtckr.foodorder.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddFoodToBasketUseCase @Inject constructor(private val repository: BasketRepository) {
    suspend operator fun invoke(context: Context, food: Food, counter: Int): Flow<ResultData<Unit>> {
        return repository.addBasket(context, food, counter)
    }
}