package com.example.aexpress.activities

import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {
    var binding: ActivityCheckoutBinding? = null
    var adapter: CartAdapter? = null
    var products: ArrayList<Product?>? = null
    var totalPrice = 0.0
    val tax = 11
    var progressDialog: ProgressDialog? = null
    var cart: Cart? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Processing...")
        products = ArrayList()
        cart = TinyCartHelper.getCart()
        for (item in cart.getAllItemsWithQty().entrySet()) {
            val product: Product = item.getKey() as Product
            val quantity: Int = item.getValue()
            product.setQuantity(quantity)
            products.add(product)
        }
        adapter = CartAdapter(this, products, object : CartListener() {
            @Override
            fun onQuantityChanged() {
                binding.subtotal.setText(String.format("PKR %.2f", cart.getTotalPrice()))
            }
        })
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.getOrientation())
        binding.cartList.setLayoutManager(layoutManager)
        binding.cartList.addItemDecoration(itemDecoration)
        binding.cartList.setAdapter(adapter)
        binding.subtotal.setText(String.format("PKR %.2f", cart.getTotalPrice()))
        totalPrice =
            cart.getTotalPrice().doubleValue() * tax / 100 + cart.getTotalPrice().doubleValue()
        binding.total.setText("PKR $totalPrice")
        binding.checkoutBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(view: View?) {
                processOrder()
            }
        })
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    }

    fun processOrder() {
        progressDialog.show()
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val productOrder = JSONObject()
        val dataObject = JSONObject()
        try {
            productOrder.put("address", binding.addressBox.getText().toString())
            productOrder.put("buyer", binding.nameBox.getText().toString())
            productOrder.put("comment", binding.commentBox.getText().toString())
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis())
            productOrder.put("last_update", Calendar.getInstance().getTimeInMillis())
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis())
            productOrder.put("email", binding.emailBox.getText().toString())
            productOrder.put("phone", binding.phoneBox.getText().toString())
            productOrder.put("serial", "cab8c1a4e4421a3b")
            productOrder.put("shipping", "")
            productOrder.put("shipping_location", "")
            productOrder.put("shipping_rate", "0.0")
            productOrder.put("status", "WAITING")
            productOrder.put("tax", tax)
            productOrder.put("total_fees", totalPrice)
            val product_order_detail = JSONArray()
            for (item in cart.getAllItemsWithQty().entrySet()) {
                val product: Product = item.getKey() as Product
                val quantity: Int = item.getValue()
                product.setQuantity(quantity)
                val productObj = JSONObject()
                productObj.put("amount", quantity)
                productObj.put("price_item", product.getPrice())
                productObj.put("product_id", product.getId())
                productObj.put("product_name", product.getName())
                product_order_detail.put(productObj)
            }
            dataObject.put("product_order", productOrder)
            dataObject.put("product_order_detail", product_order_detail)
            Log.e("err", dataObject.toString())
        } catch (e: JSONException) {
        }
        val request: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            Constants.POST_ORDER_URL,
            dataObject,
            object : Listener<JSONObject?>() {
                @Override
                fun onResponse(response: JSONObject?) {
                    try {
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Success order.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val orderNumber: String =
                                response.getJSONObject("data").getString("code")
                            Builder(this@CheckoutActivity)
                                .setTitle("Order Successful")
                                .setCancelable(false)
                                .setMessage("Your order number is: $orderNumber")
                                .setPositiveButton("Pay Now", object : OnClickListener() {
                                    @Override
                                    fun onClick(dialogInterface: DialogInterface?, i: Int) {
                                        val intent = Intent(
                                            this@CheckoutActivity,
                                            PaymentActivity::class.java
                                        )
                                        intent.putExtra("orderCode", orderNumber)
                                        startActivity(intent)
                                    }
                                }).show()
                        } else {
                            Builder(this@CheckoutActivity)
                                .setTitle("Order Failed")
                                .setMessage("Something went wrong, please try again.")
                                .setCancelable(false)
                                .setPositiveButton("Close", object : OnClickListener() {
                                    @Override
                                    fun onClick(dialogInterface: DialogInterface?, i: Int) {
                                    }
                                }).show()
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Failed order.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        progressDialog.dismiss()
                        Log.e("res", response.toString())
                    } catch (e: Exception) {
                    }
                }
            },
            object : ErrorListener() {
                @Override
                fun onErrorResponse(error: VolleyError?) {
                }
            }) {
            @Override
            @Throws(AuthFailureError::class)
            fun getHeaders(): Map<String?, String?>? {
                val headers: Map<String?, String?> = HashMap()
                headers.put("Security", "secure_code")
                return headers
            }
        }
        queue.add(request)
    }

    @Override
    fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}