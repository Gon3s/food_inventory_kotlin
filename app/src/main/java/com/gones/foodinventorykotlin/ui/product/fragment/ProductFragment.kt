package com.gones.foodinventorykotlin.ui.product.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.databinding.FragmentProductBinding
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.ui.common.extension.BaseFragment
import com.gones.foodinventorykotlin.ui.common.extension.mainNavController
import com.gones.foodinventorykotlin.ui.main.MainActivity
import com.gones.foodinventorykotlin.ui.product.event.ProductAddEvent
import com.gones.foodinventorykotlin.ui.product.viewmodel.ProductAddViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {
    private val arguments: ProductFragmentArgs by navArgs()
    private val viewModel: ProductAddViewModel by viewModel {
        parametersOf(arguments.barcode)
    }

    override fun FragmentProductBinding.initialize() {
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            launch {
                Log.i("ProductAdd", "lifecycleScope.launch")
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is ProductAddViewModel.UiEvent.ShowSnackbar -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        }
                        is ProductAddViewModel.UiEvent.SaveNote -> {
                            mainNavController().navigateUp()
                        }
                    }
                }
            }

            launch {
                viewModel.product.collect { productResource ->
                    Log.i("ProductAdd", "viewModel.getProduct().collect")
                    when (productResource) {
                        is Resource.Success -> {
                            paintView(productResource.data)
                        }

                        is Resource.Failure -> {
                            Snackbar.make(binding.root, productResource.throwable.message.toString(), Snackbar.LENGTH_LONG).show()
                        }

                        is Resource.Progress -> {
                            Snackbar.make(binding.root, "Progress...", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        viewModel.getProduct()

        (activity as MainActivity).supportActionBar?.title = "Ajouter un produit"
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_menu, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.save -> {
                viewModel.onEvent(ProductAddEvent.SaveProduct)
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    private fun paintView(product: Product) {
        Glide.with(binding.imageViewProduct.context).load(product.imageUrl).transition(
            DrawableTransitionOptions.withCrossFade()).into(binding.imageViewProduct)

        /**
         * Name
         */
        binding.productName.editText?.setText(product.productName)
        binding.productName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(ProductAddEvent.EnteredName(s.toString()))
            }
        })

        /**
         * Brands
         */
        binding.productBrands.editText?.setText(product.brands)
        binding.productBrands.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(ProductAddEvent.EnteredBrands(s.toString()))
            }
        })

        /**
         * Quantity
         */
        binding.productQuantity.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(ProductAddEvent.EnteredQuantity(s.toString().toInt()))
            }
        })
        binding.productQuantityMore.setOnClickListener {
            binding.productQuantity.editText?.setText(
                binding.productQuantity.editText?.text.toString().toInt()
                    .plus(1).toString()
            )
        }
        binding.productQuantityLess.setOnClickListener {
            var quantity = binding.productQuantity.editText?.text.toString().toInt()
                .minus(1)
            if (quantity < 1) {
                quantity = 1
            }
            binding.productQuantity.editText?.setText(
                quantity.toString()
            )
        }

        /**
         * Date
         */
        binding.productDate.editText?.setOnClickListener { v ->
            val picker: MaterialDatePicker<*> = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Date de péremption")
                .build()
            picker.addOnPositiveButtonClickListener { selection: Any ->
                Log.i("FormActivity", selection.toString())
                val date = Date(selection as Long)
                val dateFormat =
                    DateFormat.getDateFormat(context)
                binding.productDate.editText!!.setText(dateFormat.format(date))
                viewModel.onEvent(ProductAddEvent.EnteredExpiryDate(selection))
            }
            picker.show(requireActivity().supportFragmentManager, "TOTO")
        }
    }
}
