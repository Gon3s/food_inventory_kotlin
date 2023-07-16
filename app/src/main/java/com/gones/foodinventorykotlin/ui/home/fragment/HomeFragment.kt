package com.gones.foodinventorykotlin.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gones.foodinventorykotlin.databinding.FragmentHomeBinding
import com.gones.foodinventorykotlin.ui.common.extension.BaseFragment
import com.gones.foodinventorykotlin.ui.common.extension.mainNavController
import com.gones.foodinventorykotlin.ui.home.adapter.HomeAdapter
import com.gones.foodinventorykotlin.ui.home.viewmodel.HomeViewModel
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

        binding.fabScan.setOnClickListener{
            mainNavController().navigate(HomeFragmentDirections.actionHomeToScan())
        }

        viewModel.getProducts().observe(viewLifecycleOwner) { products ->
            homeAdapter.differ.submitList(products)
        }
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvSavedProducts.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}