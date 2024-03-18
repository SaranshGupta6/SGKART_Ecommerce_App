package com.example.aexpress.activities

import androidx.appcompat.app.AppCompatActivity

class CategoryActivity : AppCompatActivity() {
    var binding: ActivityCategoryBinding? = null
    var productAdapter: ProductAdapter? = null
    var products: ArrayList<Product?>? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        products = ArrayList()
        productAdapter = ProductAdapter(this, products)
        val catId: Int = getIntent().getIntExtra("catId", 0)
        val categoryName: String = getIntent().getStringExtra("categoryName")
        getSupportActionBar().setTitle(categoryName)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        getProducts(catId)
        val layoutManager = GridLayoutManager(this, 2)
        binding.productList.setLayoutManager(layoutManager)
        binding.productList.setAdapter(productAdapter)
    }

    @Override
    fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun getProducts(catId: Int) {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url: String = Constants.GET_PRODUCTS_URL + "?category_id=" + catId
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