package com.mrtckr.foodorder.domain.entity

import java.io.Serializable

data class Food(
    var id: Int,
    var name: String,
    var image_path: String,
    var price: Int
) : Serializable