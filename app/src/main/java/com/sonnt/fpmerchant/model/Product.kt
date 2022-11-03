package com.sonnt.fpmerchant.model

import com.sonnt.fpmerchant.utils.formatCurrency

data class Product(
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var price: Double? = null,
    var status: String? = null,
    var categoryId: Long? = null,
    var merchantId: Long? = null,
    var tagId: Long? = null,
    var attributes: List<ProductAttribute> = listOf()
) {
    fun getPriceStr(): String {
        return price?.formatCurrency() ?: ""
    }
}


data class ProductAttribute(
    var id: Long? = null,
    var isMultiple: Boolean = false,
    var isRequired: Boolean = false,
    var productId: Long? = null,
    var name: String? = null,
    var options: List<ProductAttributeOption> = listOf()
)

data class ProductAttributeOption(
    var id: Long? = null,
    var name: String? = null,
    var price: Double? = null,
    var productAttributeId: Long? = null
)
