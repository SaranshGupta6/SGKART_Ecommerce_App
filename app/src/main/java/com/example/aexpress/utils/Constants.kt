package com.example.aexpress.utils

import com.example.aexpress.utils.Constants
import Map.Entry
import kotlin.Throws

object Constants {
    var API_BASE_URL: String? = "https://tutorials.mianasad.com/ecommerce"
    var GET_CATEGORIES_URL: String? = API_BASE_URL.toString() + "/services/listCategory"
    var GET_PRODUCTS_URL: String? = API_BASE_URL.toString() + "/services/listProduct"
    var GET_OFFERS_URL: String? = API_BASE_URL.toString() + "/services/listFeaturedNews"
    var GET_PRODUCT_DETAILS_URL: String? =
        API_BASE_URL.toString() + "/services/getProductDetails?id="
    var POST_ORDER_URL: String? = API_BASE_URL.toString() + "/services/submitProductOrder"
    var PAYMENT_URL: String? = API_BASE_URL.toString() + "/services/paymentPage?code="
    var NEWS_IMAGE_URL: String? = API_BASE_URL.toString() + "/uploads/news/"
    var CATEGORIES_IMAGE_URL: String? = API_BASE_URL.toString() + "/uploads/category/"
    var PRODUCTS_IMAGE_URL: String? = API_BASE_URL.toString() + "/uploads/product/"
}
