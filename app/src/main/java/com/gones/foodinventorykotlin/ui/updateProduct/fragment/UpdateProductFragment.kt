package com.gones.foodinventorykotlin.ui.updateProduct.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.gones.foodinventorykotlin.databinding.FragmentUpdateProductBinding
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.ui.common.extension.BaseFragment
import com.gones.foodinventorykotlin.ui.updateProduct.viewmodel.UpdateProductViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UpdateProductFragment :
    BaseFragment<FragmentUpdateProductBinding>(FragmentUpdateProductBinding::inflate) {
    private val arguments: UpdateProductFragmentArgs by navArgs()
    private val viewModel: UpdateProductViewModel by viewModel {
        parametersOf(arguments.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.product.collect {
                        when (it) {
                            is Resource.Success -> {
                                // Handle success
                            }

                            is Resource.Failure -> {
                                // Handle failure
                            }

                            is Resource.Progress -> {
                                // Handle progress
                            }
                        }
                    }
                }
            }
        }
    }
}