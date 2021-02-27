package com.example.yemeksiparisuygulamasi.ui.menu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.yemeksiparisuygulamasi.R
import com.example.yemeksiparisuygulamasi.domain.entity.Food
import com.squareup.picasso.Picasso

class MenuAdapter(private var context:Context, private var foodList:ArrayList<Food>, private val listenerMenu: MenuItemClickListener)
    :RecyclerView.Adapter<MenuAdapter.CardViewDesignHolder>(){
    inner class CardViewDesignHolder(design:View) : RecyclerView.ViewHolder(design){
        var parentCardView : CardView = design.findViewById(R.id.parent_card_view)
        var foodNameText : TextView = design.findViewById(R.id.food_name_text_view)
        var foodImage : ImageView = design.findViewById(R.id.food_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignHolder {
        val cardDesign = LayoutInflater.from(context).inflate(R.layout.food_item_list, parent, false)
        return CardViewDesignHolder(cardDesign)
    }

    override fun onBindViewHolder(holder: CardViewDesignHolder, position: Int) {
        val food = foodList[position]
        holder.foodNameText.text = food.name
        val url = String.format(context.getString(R.string.image_base_path), food.image_path)
        Picasso.get().load(url).into(holder.foodImage)

        holder.parentCardView.setOnClickListener {
            listenerMenu.clickRow(food)
        }

    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    interface MenuItemClickListener
    {
        fun clickRow(food: Food)
    }
}