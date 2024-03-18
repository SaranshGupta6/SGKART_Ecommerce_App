package com.example.aexpress.adapters

import android.content.Context

class ProductAdapter(context: Context?, products: ArrayList<Product?>?) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder?>() {
    var context: Context?
    var products: ArrayList<Product?>?

    init {
        this.context = context
        this.products = products
    }

    @NonNull
    @Override
    fun onCreateViewHolder(@NonNull parent: ViewGroup?, viewType: Int): ProductViewHolder? {
        return ProductViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        )
    }

    @Override
    fun onBindViewHolder(@NonNull holder: ProductViewHolder?, position: Int) {
        val product: Product = products.get(position)
        Glide.with(context)
            .load(product.getImage())
            .into(holder.binding.image)
        holder.binding.label.setText(product.getName())
        holder.binding.price.setText("PKR " + product.getPrice())
        holder.itemView.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(view: View?) {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("name", product.getName())
                intent.putExtra("image", product.getImage())
                intent.putExtra("id", product.getId())
                intent.putExtra("price", product.getPrice())
                context.startActivity(intent)
            }
        })
    }

    @Override
    fun getItemCount(): Int {
        return products.size()
    }

    inner class ProductViewHolder(@NonNull itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemProductBinding?

        init {
            binding = ItemProductBinding.bind(itemView)
        }
    }
}
