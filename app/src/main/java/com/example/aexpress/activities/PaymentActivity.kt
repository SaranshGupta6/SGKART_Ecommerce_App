package com.example.aexpress.activities

import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {
    var binding: ActivityPaymentBinding? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        val orderCode: String = getIntent().getStringExtra("orderCode")
        binding.webview.setMixedContentAllowed(true)
        binding.webview.loadUrl(Constants.PAYMENT_URL + orderCode)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    }

    @Override
    fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}