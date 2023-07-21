package com.gones.foodinventorykotlin.ui.scan.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.gones.foodinventorykotlin.databinding.FragmentScanBinding
import com.gones.foodinventorykotlin.ui.common.extension.BaseFragment
import com.gones.foodinventorykotlin.ui.common.extension.mainNavController
import com.gones.foodinventorykotlin.ui.main.MainActivity
import com.google.zxing.integration.android.IntentIntegrator


class ScanFragment : BaseFragment<FragmentScanBinding>(FragmentScanBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IntentIntegrator.forSupportFragment(this).setOrientationLocked(true).setBeepEnabled(false)
            .initiateScan()

        (activity as MainActivity).supportActionBar?.title = "Scanner un produit"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                mainNavController().navigate(ScanFragmentDirections.actionScanToProduct(result.contents))
            }
        }
    }
}