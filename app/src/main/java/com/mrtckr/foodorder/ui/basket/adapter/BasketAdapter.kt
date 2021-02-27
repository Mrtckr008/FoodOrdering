package com.mrtckr.foodorder.ui.basket.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mrtckr.foodorder.R
import com.mrtckr.foodorder.domain.entity.Basket
import com.mrtckr.foodorder.domain.entity.Food
import com.squareup.picasso.Picasso

class BasketAdapter(private var myContext:Context, private var foodList:ArrayList<Basket>, private val basketListener: BasketItemClickListener)
    :RecyclerView.Adapter<BasketAdapter.CardViewDesignHolder>(){
    inner class CardViewDesignHolder(design:View) : RecyclerView.ViewHolder(design){
        var parentCardView : CardView = design.findViewById(R.id.basket_parent_card_view)
        var foodNameText : TextView = design.findViewById(R.id.food_name_basket)
        var foodImage : ImageView = design.findViewById(R.id.food_image_basket)
        var removeFoodImage : ImageView = design.findViewById(R.id.basket_delete_image)
        var foodQuantityText : TextView = design.findViewById(R.id.basket_quantity)
        var sumPriceText : TextView = design.findViewById(R.id.basket_sum_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignHolder {
        val cardDesign = LayoutInflater.from(myContext).inflate(R.layout.basket_item_list, parent, false)
        return CardViewDesignHolder(cardDesign)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewDesignHolder, position: Int) {
        val food = foodList[position]
        holder.foodNameText.text = food.food.name
        holder.foodQuantityText.text = food.orderQuantity.toString()
        holder.sumPriceText.text = "${(food.orderQuantity)*(food.food.price)} \u20BA"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.food.image_path}"
        Picasso.get().load(url).into(holder.foodImage)

        holder.removeFoodImage.setOnClickListener {
            basketListener.delete(food)
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    interface BasketItemClickListener
    {
        fun delete(foodFromBasket: Basket)
    }
}