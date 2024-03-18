package com.example.aexpress.activities

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    var categoryAdapter: CategoryAdapter? = null
    var categories: ArrayList<Category?>? = null
    var productAdapter: ProductAdapter? = null
    var products: ArrayList<Product?>? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        binding.searchBar.setOnSearchActionListener(object : OnSearchActionListener() {
            @Override
            fun onSearchStateChanged(enabled: Boolean) {
            }

            @Override
            fun onSearchConfirmed(text: CharSequence?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("query", text.toString())
                startActivity(intent)
            }

            @Override
            fun onButtonClicked(buttonCode: Int) {
            }
        })
        initCategories()
        initProducts()
        initSlider()
    }

    private fun initSlider() {
        getRecentOffers()
    }

    fun initCategories() {
        categories = ArrayList()
        categoryAdapter = CategoryAdapter(this, categories)
        getCategories()
        val layoutManager = GridLayoutManager(this, 4)
        binding.categoriesList.setLayoutManager(layoutManager)
        binding.categoriesList.setAdapter(categoryAdapter)
    }

    fun getCategories() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET,
            Constants.GET_CATEGORIES_URL,
            object : Listener<String?>() {
                @Override
                fun onResponse(response: String?) {
                    try {
                        Log.e("err", response)
                        val mainObj = JSONObject(response)
                        if (mainObj.getString("status").equals("success")) {
                            val categoriesArray: JSONArray = mainObj.getJSONArray("categories")
                            for (i in 0 until categoriesArray.length()) {
                                val `object`: JSONObject = categoriesArray.getJSONObject(i)
                                val category = Category(
                                    `object`.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + `object`.getString("icon"),
                                    `object`.getString("color"),
                                    `object`.getString("brief"),
                                    `object`.getInt("id")
                                )
                                categories.add(category)
                            }
                            categoryAdapter.notifyDataSetChanged()
                        } else {
                            // DO nothing
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            object : ErrorListener() {
                @Override
                fun onErrorResponse(error: VolleyError?) {
                }
            })
        queue.add(request)
    }

    fun getRecentProducts() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url: String = Constants.GET_PRODUCTS_URL + "?count=8"
        val request = StringRequest(Request.Method.GET, url, { response ->
            try {
                val `object` = JSONObject(response)
                if (`object`.getString("status").equals("success")) {
                    val productsArray: JSONArray = `object`.getJSONArray("products")
                    for (i in 0 until productsArray.length()) {
                        val childObj: JSONObject = productsArray.getJSONObject(i)
                        val product = Product(
                            childObj.getString("name"),
                            Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                            childObj.getString("status"),
                            childObj.getDouble("price"),
                            childObj.getDouble("price_discount"),
                            childObj.getInt("stock"),
                            childObj.getInt("id")
                        )
                        products.add(product)
                    }
                    productAdapter.notifyDataSetChanged()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error -> }
        queue.add(request)
    }

    fun getRecentOffers() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, { response ->
            try {
                val `object` = JSONObject(response)
                if (`object`.getString("status").equals("success")) {
                    val offerArray: JSONArray = `object`.getJSONArray("news_infos")
                    for (i in 0 until offerArray.length()) {
                        val childObj: JSONObject = offerArray.getJSONObject(i)
                        binding.carousel.addData(
                            CarouselItem(
                                Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("title")
                            )
                        )
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error -> }
        queue.add(request)
    }

    fun initProducts() {
        products = ArrayList()
        productAdapter = ProductAdapter(this, products)
        getRecentProducts()
        val layoutManager = GridLayoutManager(this, 2)
        binding.productList.setLayoutManager(layoutManager)
        binding.productList.setAdapter(productAdapter)
    }
}