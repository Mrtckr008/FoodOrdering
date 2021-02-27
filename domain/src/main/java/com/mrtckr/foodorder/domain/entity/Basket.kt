package com.mrtckr.foodorder.domain.entity

import java.io.Serializable

data class Basket(var food: Food, var orderQuantity: Int) : Serializable