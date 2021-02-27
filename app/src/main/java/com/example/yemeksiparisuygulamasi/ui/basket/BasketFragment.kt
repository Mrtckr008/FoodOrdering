package com.example.yemeksiparisuygulamasi.ui.basket

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.yemeksiparisuygulamasi.R
import com.example.yemeksiparisuygulamasi.databinding.FragmentBasketBinding
import com.example.yemeksiparisuygulamasi.domain.entity.Basket
import com.example.yemeksiparisuygulamasi.domain.entity.Food
import com.example.yemeksiparisuygulamasi.domain.entity.ResultData
import com.example.yemeksiparisuygulamasi.ui.basket.adapter.BasketAdapter
import com.example.yemeksiparisuygulamasi.ui.common.BaseFragment
import com.example.yemeksiparisuygulamasi.ui.menu.adapter.MenuAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketFragment : BaseFragment<BasketViewModel, FragmentBasketBinding>(){

    override val layoutRes: Int = R.layout.fragment_basket
    override val viewModel: BasketViewModel by viewModels()

    override fun observeViewModel() {
        viewModel.foodsListFromBasket.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Success -> {
                    var sumPrice = 0
                    binding.basketRecyclerView.setHasFixedSize(true)
                    binding.basketRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    binding.basketRecyclerView.adapter =
                        BasketAdapter(
                            this.requireContext(),
                            it.toData() as ArrayList<Basket>,
                            object : BasketAdapter.BasketItemClickListener {
                                override fun delete(foodFromBasket: Basket) {
                                    viewModel.deleteFoodsFromBasket(this@BasketFragment.requireContext(),foodFromBasket)
                                }
                            })
                    binding.basketRecyclerView.visibility = View.VISIBLE
                    binding.noProductInBasketText.visibility = View.GONE

                    it.data?.forEach {
                        sumPrice += (it.food.price * it.orderQuantity)
                    }
                    binding.sumBasketPriceText.text = sumPrice.toString()
                }
                is ResultData.Failed -> {
                    binding.basketRecyclerView.visibility = View.GONE
                    binding.noProductInBasketText.visibility = View.VISIBLE
                }
                is ResultData.Loading -> {

                }
            }
        })

        viewModel.removedFoodToBasket.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Success -> {
                    Toast.makeText(this.requireContext(), this.requireContext().getString(R.string.removed_successfully), Toast.LENGTH_SHORT).show()

                    viewModel.getFoodsFromBasket(this.requireContext())
                }
                is ResultData.Failed -> {
                    Toast.makeText(this.requireContext(), this.requireContext().getString(R.string.removed_successfully), Toast.LENGTH_SHORT).show()

                }
                is ResultData.Loading -> {

                }
            }
        })
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getFoodsFromBasket(this.requireContext())
    }
}