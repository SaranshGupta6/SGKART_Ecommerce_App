package com.example.aexpress.adapters

import android.content.Context

class CartAdapter(context: Context?, products: ArrayList<Product?>?, cartListener: CartListener?) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder?>() {
    var context: Context?
    var products: ArrayList<Product?>?
    var cartListener: CartListener?
    var cart: Cart?

    interface CartListener {
        open fun onQuantityChanged()
    }

    init {
        this.context = context
        this.products = products
        this.cartListener = cartListener
        cart = TinyCartHelper.getCart()
    }

    @NonNull
    @Override
    fun onCreateViewHolder(@NonNull parent: ViewGroup?, viewType: Int): CartViewHolder? {
        return CartViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        )
    }

    @Override
    fun onBindViewHolder(@NonNull holder: CartViewHolder?, position: Int) {
        val product: Product = products.get(position)
        Glide.with(context)
            .load(product.getImage())
            .into(holder.binding.image)
        holder.binding.name.setText(product.getName())
        holder.binding.price.setText("PKR " + product.getPrice())
        holder.binding.quantity.setText(product.getQuantity() + " item(s)")
        holder.itemView.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(view: View?) {
                val quantityDialogBinding: QuantityDialogBinding =
                    QuantityDialogBinding.inflate(LayoutInflater.from(context))
                val dialog: AlertDialog = Builder(context)
                    .setView(quantityDialogBinding.getRoot())
                    .create()
                dialog.getWindow().setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
                quantityDialogBinding.productName.setText(product.getName())
                quantityDialogBinding.productStock.setText("Stock: " + product.getStock())
                quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()))
                val stock: Int = product.getStock()
                quantityDialogBinding.plusBtn.setOnClickListener(object : OnClickListener() {
                    @Override
                    fun onClick(view: View?) {
                        var quantity: Int = product.getQuantity()
                        quantity++
                        if (quantity > product.getStock()) {
                            Toast.makeText(
                                context,
                                "Max stock available: " + product.getStock(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        } else {
                            product.setQuantity(quantity)
                            quantityDialogBinding.quantity.setText(String.valueOf(quantity))
                        }
                        notifyDataSetChanged()
                        cart.updateItem(product, product.getQuantity())
                        cartListener.onQuantityChanged()
                    }
                })
                quantityDialogBinding.minusBtn.setOnClickListener(object : OnClickListener() {
                    @Override
                    fun onClick(view: View?) {
                        var quantity: Int = product.getQuantity()
                        if (quantity > 1) quantity--
                        product.setQuantity(quantity)
                        quantityDialogBinding.quantity.setText(String.valueOf(quantity))
                        notifyDataSetChanged()
                        cart.updateItem(product, product.getQuantity())
                        cartListener.onQuantityChanged()
                    }
                })
                quantityDialogBinding.saveBtn.setOnClickListener(object : OnClickListener() {
                    @Override
                    fun onClick(view: View?) {
                        dialog.dismiss()
                        //                        notifyDataSetChanged();
//                        cart.updateItem(product, product.getQuantity());
//                        cartListener.onQuantityChanged();
                    }
                })
                dialog.show()
            }
        })
    }

    @Override
    fun getItemCount(): Int {
        return products.size()
    }

    inner class CartViewHolder(@NonNull itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemCartBinding?

        init {
            binding = ItemCartBinding.bind(itemView)
        }
    }
}
