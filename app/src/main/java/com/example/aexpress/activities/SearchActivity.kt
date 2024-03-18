package com.example.aexpress.activities

import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    var binding: ActivitySearchBinding? = null
    var productAdapter: ProductAdapter? = null
    var products: ArrayList<Product?>? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        products = ArrayList()
        productAdapter = ProductAdapter(this, products)
        val query: String = getIntent().getStringExtra("query")
        getSupportActionBar().setTitle(query)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        getProducts(query)
        val layoutManager = GridLayoutManager(this, 2)
        binding.productList.setLayoutManager(layoutManager)
        binding.productList.setAdapter(productAdapter)
    }

    @Override
    fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun getProducts(query: String?) {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url: String = Constants.GET_PRODUCTS_URL + "?q=" + query
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
}