# BaseFragment

    abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment() {
    abstract val layoutRes: Int
    abstract val viewModel: T

    open fun initBinding() {}
    abstract fun observeViewModel()
    abstract fun viewCreated(view: View, savedInstanceState: Bundle?)


    private var _binding: B? = null
    val binding get() = _binding!!

    companion object {
        private const val TAG = "BaseFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        initBinding()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated(view, savedInstanceState)
        observeLoadingAndError()
        observeViewModel()
    }

    private fun observeLoadingAndError() {
        viewModel.loadingErrorState.observe(viewLifecycleOwner) { it ->
            when (it) {
                is ResultData.Loading -> {
                    showLoading()
                }
                is ResultData.Success -> {
                    hideLoading()
                }
                is ResultData.Failed -> {
                    hideLoading()
                    //showErrorDialog(it.error)
                }
            }
        }
    }

    private val loadingAlertDialog by lazy {
        context?.let {
            Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setContentView(R.layout.dialog_loading)
                setCancelable(false)
            }
        }
    }

    private fun showLoading() {
        Log.i(TAG, "showLoading: ")
        loadingAlertDialog?.show()
    }

    private fun hideLoading() {
        Log.i(TAG, "hideLoading: ")
        loadingAlertDialog?.dismiss()
    }

    private fun showErrorDialog(message: String?, callback: () -> Unit = {}) {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Warning")
                setMessage(message)
                setPositiveButton("Close") { _, _ -> callback.invoke() }
            }.show()
        }
    }
    }

# Example Fragment

    @AndroidEntryPoint
    class BasketFragment : BaseFragment<BasketViewModel, BasketFragmentBinding>() {
    override val layoutRes: Int = R.layout.basket_fragment
    override val viewModel: BasketViewModel by viewModels()
    override fun observeViewModel() {
        viewModel.foodsListFromBasket.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is ResultData.Success -> {

                }
                is ResultData.Failed -> {

                }
                is ResultData.Loading -> {
                }
            }
        })

        viewModel.removedFoodToBasket.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Success -> {

                }
                is ResultData.Failed -> {
                    
                }
                is ResultData.Loading -> {
                }
            }
        })
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getFoodsFromBasket(this.requireContext())
    }
    }
    
# Example ViewModel
    
    class BasketViewModel @ViewModelInject constructor(
    private val getFoodsFromBasketUseCase: GetFoodsFromBasketUseCase,
    private val removeFoodFromBasketUseCase: RemoveFoodFromBasketUseCase) : BaseViewModel() {
    private val _removedFoodToBasket = MutableLiveData<ResultData<Unit>>()
    val removedFoodToBasket: MutableLiveData<ResultData<Unit>>
        get() = _removedFoodToBasket

    private val _foodsListFromBasket = MutableLiveData<ResultData<List<Basket>>>()
    val foodsListFromBasket: MutableLiveData<ResultData<List<Basket>>>
        get() = _foodsListFromBasket

    fun getFoodsFromBasket(context:Context){
        viewModelScope.launch(Dispatchers.IO) {
            getFoodsFromBasketUseCase.invoke(context).collect { it ->
                handleTask(it) {
                    foodsListFromBasket.postValue(it)
                }
            }
        }
    }

    fun deleteFoodsFromBasket(context:Context, foodFromBasket: Basket){
        viewModelScope.launch(Dispatchers.IO) {
            removeFoodFromBasketUseCase.invoke(context,foodFromBasket).collect { it ->
                handleTask(it) {
                    removedFoodToBasket.postValue(it)
                }
            }
        }
    }
    }
    
# Example UseCase

    class AddFoodToBasketUseCase @Inject constructor(private val repository: BasketRepository) {
      suspend operator fun invoke(context: Context, food: Food, counter: Int): Flow<ResultData<Unit>> {
          return repository.addBasket(context, food, counter)
      }
    }
    
# ResultData

    sealed class ResultData<out T> {
      data class Loading(val nothing: Nothing? = null) : ResultData<Nothing>()
      data class Success<out T>(val data: T? = null) : ResultData<T>()
      data class Failed(val error: String? = null) : ResultData<Nothing>()

      fun toData(): T? = if(this is Success) this.data else null
    }
    
# BasketRepository in Domain module.

    interface BasketRepository {
      suspend fun addBasket(context: Context, food: Food, counter: Int): Flow<ResultData<Unit>>
      suspend fun removeBasket(context: Context, foodFromBasket: Basket): Flow<ResultData<Unit>>
      suspend fun getBasket(context: Context):  Flow<ResultData<List<Basket>>>
    }
  
# BasketRepositoryImpl in Data module.

    class BasketRepositoryImpl @Inject constructor(private val dataSource: BasketRemoteDataSource) : BasketRepository {

    override suspend fun addBasket(context: Context, food: Food, counter: Int): Flow<ResultData<Unit>> {
        return dataSource.addBasket(context, food, counter)
    }

    override suspend fun removeBasket(context: Context, foodFromBasket: Basket): Flow<ResultData<Unit>> {
        return dataSource.removeBasket(context,foodFromBasket)
    }

    override suspend fun getBasket(context: Context): Flow<ResultData<List<Basket>>> = flow {
        emit(ResultData.Loading())
        val commentsList = dataSource.getBasket(context)
        commentsList.collect {
            if (it != null){
                emit(ResultData.Success(it))
            }
            else{
                emit(ResultData.Failed())
            }
        }
    }
    }
    
    
# BasketRemoteDataSource in Data module.

    interface BasketRemoteDataSource {
      suspend fun addBasket(context: Context, food: Food, counter: Int): Flow<ResultData<Unit>>
      suspend fun removeBasket(context: Context, foodFromBasket: Basket): Flow<ResultData<Unit>>
      suspend fun getBasket(context: Context): Flow<List<Basket>?>
    }
    
# BasketRemoteDataSourceImpl in Data module. 
Not: I used Volley for api operations. Because I wanted to try it to compare with Retrofit. USING RETROFIT IS RECOMMENDED.

    class BasketRemoteDataSourceImpl @Inject constructor() :
    BasketRemoteDataSource {
    override suspend fun addBasket(
        context: Context,
        food: Food,
        counter: Int
    ): Flow<ResultData<Unit>> {
        return flowViaChannel { flowChannel ->
            val webServiceUrl = "http://kasimadalan.pe.hu/yemekler/insert_sepet_yemek.php"
            val requestToUrl = object : StringRequest(
                Method.POST,
                webServiceUrl,
                Response.Listener { responseOfUrl ->
                    flowChannel.sendBlocking(ResultData.Success())
                }, Response.ErrorListener {
                    flowChannel.sendBlocking(ResultData.Failed())
                }) {

                override fun getParams(): MutableMap<String, String> {
                    val parameter = HashMap<String, String>()
                    parameter["yemek_siparis_adet"] = counter.toString()
                    parameter["yemek_id"] = food.id.toString()
                    parameter["yemek_adi"] = food.name
                    parameter["yemek_resim_adi"] = food.image_path
                    parameter["yemek_fiyat"] = food.price.toString()
                    return parameter
                }
            }

            Volley.newRequestQueue(context).add(requestToUrl)
         }
      }
      
# DI provide repositories and datasources with Hilt.
  
    @InstallIn(ActivityComponent::class)
    @Module
    object RepositoryModule {

    @Provides
    fun providesMenuRepository(
        remoteDataSource: MenuRemoteDataSource
    ): MenuRepository {
        return MenuRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    fun providesMenuRemoteDataSource(): MenuRemoteDataSource {
        return MenuRemoteDataSourceImpl()
    }

    @Provides
    fun providesBasketRepository(
        remoteDataSource: BasketRemoteDataSource
    ): BasketRepository {
        return BasketRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    fun providesBasketRemoteDataSource(): BasketRemoteDataSource {
        return BasketRemoteDataSourceImpl()
    }
    }
    
# DI provide usecases with Hilt.

    @InstallIn(ActivityComponent::class)
    @Module
    object UseCaseModule {
    @Provides
    fun providesGetAllFoodsUseCase(repository: MenuRepository): GetAllFoodsUseCase {
        return GetAllFoodsUseCase(repository)
    }

    @Provides
    fun providesSearchFoodWithKeywordUseCase(repository: MenuRepository): SearchFoodsWithNameUseCase {
        return SearchFoodsWithNameUseCase(repository)
    }

    @Provides
    fun providesAddFoodToBasketUseCase(repository: BasketRepository): AddFoodToBasketUseCase {
        return AddFoodToBasketUseCase(repository)
    }

    @Provides
    fun providesRemoveFoodFromBasketUseCase(repository: BasketRepository): RemoveFoodFromBasketUseCase {
        return RemoveFoodFromBasketUseCase(repository)
    }

    @Provides
    fun providesGetAllFoodsFromBasketUseCase(repository: BasketRepository): GetFoodsFromBasketUseCase {
        return GetFoodsFromBasketUseCase(repository)
    }
    }
