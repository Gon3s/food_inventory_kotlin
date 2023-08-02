package com.gones.foodinventorykotlin.ui.home.fragment


/*class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel {
        parametersOf()
    }
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        return super.onCreateView(inflater, container, savedInstanceState)
    }

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
}*/
