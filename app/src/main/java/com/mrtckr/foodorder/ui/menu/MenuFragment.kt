package com.mrtckr.foodorder.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mrtckr.foodorder.R
import com.mrtckr.foodorder.databinding.MenuFragmentBinding
import com.mrtckr.foodorder.ui.menu.adapter.MenuAdapter
import com.mrtckr.foodorder.domain.entity.Food
import com.mrtckr.foodorder.domain.entity.ResultData
import com.mrtckr.foodorder.ui.common.BaseFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.alertview_layout.view.*

@AndroidEntryPoint
open class MenuFragment : BaseFragment<MenuViewModel, MenuFragmentBinding>() {
    override val layoutRes: Int = R.layout.menu_fragment
    override val viewModel: MenuViewModel by viewModels()
    private lateinit var navController: NavController

    override fun observeViewModel() {
        viewModel.foodList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Success -> {
                    binding.menuRecyclerView.setHasFixedSize(true)
                    binding.menuRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    binding.menuRecyclerView.adapter =
                        MenuAdapter(
                            this.requireContext(),
                            it.toData() as ArrayList<Food>,
                            object : MenuAdapter.MenuItemClickListener {
                                override fun clickRow(food: Food) {
                                    alertView(food)
                                }
                            })
                }
                is ResultData.Failed -> {
                    Toast.makeText(this.requireContext(), this.requireContext().getString(R.string.products_get_error), Toast.LENGTH_SHORT).show()
                }
                is ResultData.Loading -> {

                }
            }
        })

        viewModel.searchedFoodList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Success -> {
                    binding.menuRecyclerView.setHasFixedSize(true)
                    binding.menuRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    binding.menuRecyclerView.adapter =
                        MenuAdapter(
                            this.requireContext(),
                            it.toData() as ArrayList<Food>,
                            object : MenuAdapter.MenuItemClickListener {
                                override fun clickRow(food: Food) {
                                    alertView(food)
                                }
                            })
                }
                is ResultData.Failed -> {
                    Toast.makeText(this.requireContext(), this.requireContext().getString(R.string.no_search_result), Toast.LENGTH_SHORT).show()
                }
                is ResultData.Loading -> {

                }
            }
        })

        viewModel.addedFoodToBasket.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Success -> {
                    Toast.makeText(this.requireContext(), this.requireContext().getString(R.string.added_successfully), Toast.LENGTH_SHORT).show()
                }
                is ResultData.Failed -> {
                    Toast.makeText(this.requireContext(), this.requireContext().getString(R.string.added_failed), Toast.LENGTH_SHORT).show()
                }
                is ResultData.Loading -> {

                }
            }
        })
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        viewModel.getAllFoods(this.requireContext())
        binding.menuSearchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.menuSearchView.query?.let { viewModel.searchFoodsWithKeyword(this@MenuFragment.requireContext(), it.toString()) }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                binding.menuSearchView.query?.let { viewModel.searchFoodsWithKeyword(this@MenuFragment.requireContext(), it.toString()) }
                return true
            }
        })
    }

    fun alertView(food :Food){
        val design = LayoutInflater.from(this.requireContext()).inflate(R.layout.alertview_layout, null)

        val alert = AlertDialog.Builder(this.requireContext())
            .setTitle(this.requireContext().getString(R.string.product_detail))
            .setView(design)

        var counter = 1
        design.order_counter_text.text = counter.toString()
        design.alert_text.text = String.format(
            this.requireContext().getString(R.string.add_product_alert_dialog_text),
            food.name,
            food.price
        )

        val url = String.format(
            this.requireContext().getString(R.string.image_base_path),
            food.image_path)

        Picasso.get().load(url).into(design.alert_food_image)

        design.add_button.setOnClickListener {
            counter++
            design.order_counter_text.text = counter.toString()
        }
        design.remove_button.setOnClickListener {
            if(counter != 1) {
                counter--
                design.order_counter_text.text = counter.toString()
            }
        }

        alert.setPositiveButton(this.requireContext().getString(R.string.add_to_basket)){ dialogInterface, i->
            viewModel.addFoodsToBasket(this@MenuFragment.requireContext(),food,counter)
        }
        alert.setNegativeButton(this.requireContext().getString(R.string.cancel)){ dialogInterface, i->
            dialogInterface.dismiss()
        }
        alert.create().show()
    }
}