package com.mrtckr.foodorder.domain.usecase

import android.content.Context
import com.mrtckr.foodorder.domain.entity.Basket
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.domain.repository.BasketRepository
import com.mrtckr.foodorder.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodsFromBasketUseCase @Inject constructor(private val repository: BasketRepository) {
    operator fun invoke(context: Context): Flow<ResultData<List<Basket>>> {
        return repository.getBasket(context)
    }
}