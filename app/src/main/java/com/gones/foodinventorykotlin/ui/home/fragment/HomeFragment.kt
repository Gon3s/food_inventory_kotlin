package com.gones.foodinventorykotlin.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gones.foodinventorykotlin.databinding.FragmentHomeBinding
import com.gones.foodinventorykotlin.ui.common.extension.BaseFragment
import com.gones.foodinventorykotlin.ui.common.extension.mainNavController
import com.gones.foodinventorykotlin.ui.home.adapter.HomeAdapter
import com.gones.foodinventorykotlin.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel {
        parametersOf()
    }
    lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.fabScan.setOnClickListener {
            mainNavController().navigate(HomeFragmentDirections.actionHomeToScan())
        }

        lifecycle.coroutineScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.getProducts().collect { products ->
                    withContext(Dispatchers.Main) {
                        homeAdapter.submitList(products)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvSavedProducts.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity)
            homeAdapter.setOnItemClickListener {
                mainNavController().navigate(HomeFragmentDirections.actionHomeToProduct(id = it.id.toString()))
            }
        }
    }
}