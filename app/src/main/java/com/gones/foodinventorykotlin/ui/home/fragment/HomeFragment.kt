package com.gones.foodinventorykotlin.ui.home.fragment

import android.os.Bundle
import android.view.View
import com.gones.foodinventorykotlin.databinding.FragmentHomeBinding
import com.gones.foodinventorykotlin.ui.common.extension.BaseFragment
import com.gones.foodinventorykotlin.ui.common.extension.mainNavController


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeScan.setOnClickListener{
            mainNavController().navigate(HomeFragmentDirections.actionHomeToScan())
        }
    }
}