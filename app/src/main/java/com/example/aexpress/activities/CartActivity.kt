package com.example.aexpress.activities

import androidx.appcompat.app.AlertDialog

class CartActivity : AppCompatActivity() {
    var binding: ActivityCartBinding? = null
    var adapter: CartAdapter? = null
    var products: ArrayList<Product?>? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        products = ArrayList()
        val cart: Cart = TinyCartHelper.getCart()
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
        binding.continueBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(view: View?) {
                startActivity(Intent(this@CartActivity, CheckoutActivity::class.java))
            }
        })
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    }

    @Override
    fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}