package com.example.aexpress.activities

import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {
    var binding: ActivityProductDetailBinding? = null
    var currentProduct: Product? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        val name: String = getIntent().getStringExtra("name")
        val image: String = getIntent().getStringExtra("image")
        val id: Int = getIntent().getIntExtra("id", 0)
        val price: Double = getIntent().getDoubleExtra("price", 0)
        Glide.with(this)
            .load(image)
            .into(binding.productImage)
        getProductDetails(id)
        getSupportActionBar().setTitle(name)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        val cart: Cart = TinyCartHelper.getCart()
        binding.addToCartBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(view: View?) {
                cart.addItem(currentProduct, 1)
                binding.addToCartBtn.setEnabled(false)
                binding.addToCartBtn.setText("Added in cart")
            }
        })
    }

    @Override
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @Override
    fun onOptionsItemSelected(@NonNull item: MenuItem?): Boolean {
        if (item.getItemId() === R.id.cart) {
            startActivity(Intent(this, CartActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    fun getProductDetails(id: Int) {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url: String = Constants.GET_PRODUCT_DETAILS_URL + id
        val request = StringRequest(Request.Method.GET, url, object : Listener<String?>() {
            @Override
            fun onResponse(response: String?) {
                try {
                    val `object` = JSONObject(response)
                    if (`object`.getString("status").equals("success")) {
                        val product: JSONObject = `object`.getJSONObject("product")
                        val description: String = product.getString("description")
                        binding.productDescription.setText(
                            Html.fromHtml(description)
                        )
                        currentProduct = Product(
                            product.getString("name"),
                            Constants.PRODUCTS_IMAGE_URL + product.getString("image"),
                            product.getString("status"),
                            product.getDouble("price"),
                            product.getDouble("price_discount"),
                            product.getInt("stock"),
                            product.getInt("id")
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, object : ErrorListener() {
            @Override
            fun onErrorResponse(error: VolleyError?) {
            }
        })
        queue.add(request)
    }

    @Override
    fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}