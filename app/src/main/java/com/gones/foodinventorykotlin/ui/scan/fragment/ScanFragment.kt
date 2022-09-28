package com.gones.foodinventorykotlin.ui.scan.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.gones.foodinventorykotlin.databinding.FragmentScanBinding
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.ui.common.extension.BaseFragment
import com.gones.foodinventorykotlin.ui.common.extension.mainNavController
import com.gones.foodinventorykotlin.ui.main.MainActivity
import com.gones.foodinventorykotlin.ui.scan.viewmodel.ScanViewModel
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.androidx.viewmodel.ext.android.viewModel


class ScanFragment : BaseFragment<FragmentScanBinding>(FragmentScanBinding::inflate) {

//    private val viewModel: ScanViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initViewObserver()
        IntentIntegrator.forSupportFragment(this).setOrientationLocked(true).setBeepEnabled(false).initiateScan()

        (activity as MainActivity).supportActionBar?.title = "Scanner un produit"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                mainNavController().navigate(ScanFragmentDirections.actionScanToProduct(result.contents))
                // viewModel.getProduct(result.contents)
            }
        }
    }

    /*private fun initViewObserver() {
        viewModel.getProductResult.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                mainNavController().navigate(ScanFragmentDirections.actionScanToProduct(it.data))
            } else if (it is Resource.Failure) {
                Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}