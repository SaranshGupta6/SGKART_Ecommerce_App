package com.example.aexpress.model

import com.hishd.tinycart.model.Item

class Product(
    private var name: String?,
    private var image: String?,
    private var status: String?,
    private var price: Double,
    private var discount: Double,
    private var stock: Int,
    private var id: Int
) : Item, Serializable {
    private var quantity = 0
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getPrice(): Double {
        return price
    }

    fun setPrice(price: Double) {
        this.price = price
    }

    fun getDiscount(): Double {
        return discount
    }

    fun setDiscount(discount: Double) {
        this.discount = discount
    }

    fun getStock(): Int {
        return stock
    }

    fun setStock(stock: Int) {
        this.stock = stock
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    @Override
    fun getItemPrice(): BigDecimal? {
        return BigDecimal(price)
    }

    @Override
    fun getItemName(): String? {
        return name
    }

    fun getQuantity(): Int {
        return quantity
    }

    fun setQuantity(quantity: Int) {
        this.quantity = quantity
    }
}
