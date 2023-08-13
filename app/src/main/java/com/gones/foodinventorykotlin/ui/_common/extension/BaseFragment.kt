package com.gones.foodinventorykotlin.ui._common.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseFragment<Binding : ViewBinding>(private val inflateMethod: (LayoutInflater, ViewGroup?, Boolean) -> Binding) :
    Fragment() {
    private var _binding: Binding? = null
    val binding get() = _binding!!

    // Make it open, so it can be overridden in child fragments
    open fun Binding.initialize() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflateMethod(inflater, container, false)

        binding.initialize()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}