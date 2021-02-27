package com.mrtckr.foodorder.domain.usecase

import android.content.Context
import com.mrtckr.foodorder.domain.entity.Basket
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.domain.repository.BasketRepository
import com.mrtckr.foodorder.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFoodFromBasketUseCase @Inject constructor(private val repository: BasketRepository) {
    suspend operator fun invoke(context: Context, foodFromBasket: Basket): Flow<ResultData<Unit>> {
        return repository.removeBasket(context,foodFromBasket)
    }
}