package com.gones.foodinventorykotlin.ui.product.fragment

/*class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {
    private val arguments: ProductFragmentArgs by navArgs()
    private val viewModel: ProductViewModel by viewModel {
        parametersOf(arguments.barcode, arguments.id)
    }
    private lateinit var otherProductAdapter: OtherProductAdapter

    override fun FragmentProductBinding.initialize() {
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        otherProductAdapter = OtherProductAdapter()
        binding.recyclerViewOtherProducts.apply {
            adapter = otherProductAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.eventFlow.collectLatest { event ->
                        when (event) {
                            is ProductViewModel.UiEvent.ShowSnackbar -> {
                                Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG)
                                    .show()
                            }

                            is ProductViewModel.UiEvent.ProductCreated -> {
                                mainNavController().navigateUp()
                            }

                            is ProductViewModel.UiEvent.ProductUpdated -> {
                                mainNavController().navigateUp()
                            }
                        }
                    }
                }

                launch {
                    viewModel.product.collect { productResource ->
                        Timber.d("DLOG: product.collect: $productResource - ${viewModel.type}")

                        when (productResource) {
                            is Resource.Success -> {
                                binding.linearLayoutMain.visibility = View.VISIBLE
                                binding.linearLayoutWaiting.visibility = View.GONE
                                // Todo: Product Not Found
                                paintView(productResource.data)
                            }

                            is Resource.Failure -> {
                                binding.progressBarMain.visibility = View.GONE
                                binding.textviewMainError.text = productResource.throwable.message
                                binding.textviewMainError.visibility = View.VISIBLE
                                binding.linearLayoutMain.visibility = View.GONE
                                binding.linearLayoutWaiting.visibility = View.VISIBLE
                            }

                            is Resource.Progress -> {
                                binding.textviewMainError.visibility = View.GONE
                                binding.progressBarMain.visibility = View.VISIBLE
                                binding.linearLayoutMain.visibility = View.GONE
                                binding.linearLayoutWaiting.visibility = View.VISIBLE
                            }
                        }

                        if (viewModel.type == ProductViewModel.TYPES.UPDATE) {
                            binding.linearLayoutOtherProducts.visibility = View.GONE
                            binding.linearLayoutQuantity.visibility = View.GONE
                        } else {
                            binding.linearLayoutOtherProducts.visibility = View.VISIBLE
                            binding.linearLayoutQuantity.visibility = View.VISIBLE
                            binding.buttonConsumeProduct.visibility = View.GONE
                            binding.linearLayoutConsumeProduct.visibility = View.GONE
                        }
                    }
                }

                launch {
                    viewModel.products.collect {
                        when (it) {
                            is Resource.Success -> {
                                binding.recyclerViewOtherProducts.visibility = View.VISIBLE
                                binding.progressBarOtherProducts.visibility = View.GONE
                                binding.textviewMainError.visibility = View.GONE

                                if (it.data.isEmpty()) {
                                    binding.frameLayoutOtherProducts.visibility = View.VISIBLE
                                    binding.textviewNoProduct.visibility = View.VISIBLE
                                } else {
                                    otherProductAdapter.submitList(it.data)

                                    binding.frameLayoutOtherProducts.visibility = View.VISIBLE
                                    binding.textviewNoProduct.visibility = View.GONE
                                }
                            }

                            is Resource.Failure -> {
                                binding.frameLayoutOtherProducts.visibility = View.VISIBLE
                                binding.recyclerViewOtherProducts.visibility = View.GONE
                                binding.progressBarOtherProducts.visibility = View.GONE
                                binding.textviewNoProduct.visibility = View.GONE
                                binding.textviewError.text = it.throwable.message
                                binding.textviewMainError.visibility = View.VISIBLE
                            }

                            is Resource.Progress -> {
                                binding.frameLayoutOtherProducts.visibility = View.VISIBLE
                                binding.recyclerViewOtherProducts.visibility = View.GONE
                                binding.textviewError.visibility = View.GONE
                                binding.textviewNoProduct.visibility = View.GONE
                                binding.progressBarOtherProducts.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        viewModel.getProduct()

        /*(activity as MainActivity).supportActionBar?.title =
            (viewModel.type == ProductViewModel.TYPES.UPDATE).let {
                if (it) {
                    "Modifier un produit"
                } else {
                    "Ajouter un produit"
                }
            }*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_menu, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
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
            DrawableTransitionOptions.withCrossFade()
        ).into(binding.imageViewProduct)

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
                val date = Date(selection as Long)
                val dateFormat = DateFormat.getDateFormat(context)
                binding.productDate.editText?.setText(dateFormat.format(date))
                viewModel.onEvent(ProductAddEvent.EnteredExpiryDate(selection))
            }
            picker.show(requireActivity().supportFragmentManager, "TOTO")
        }

        binding.textviewMainError.visibility = View.GONE
        binding.progressBarMain.visibility = View.GONE

        if (product.consumed) {
            binding.buttonConsumeProduct.visibility = View.GONE
            binding.linearLayoutConsumeProduct.visibility = View.VISIBLE
            product.consumed_at?.let { consumed_at ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binding.textViewConsumedAt.text =
                        DateFormat.getDateFormat(context)
                            .format(Date.from(consumed_at.toJavaInstant()))
                } else {
                    //TODO: format Instant date for API < 26
                    binding.textViewConsumedAt.text = consumed_at.toString()
                }
            }
        } else {
            binding.buttonConsumeProduct.visibility = View.VISIBLE
            binding.linearLayoutConsumeProduct.visibility = View.GONE

            binding.buttonConsumeProduct.setOnClickListener {
                viewModel.onEvent(ProductAddEvent.Consume)
            }
        }


    }
}
*/