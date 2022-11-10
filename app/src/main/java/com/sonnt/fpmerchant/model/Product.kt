package com.sonnt.fpmerchant.model

import android.os.Parcelable
import com.sonnt.fpmerchant.utils.formatCurrency
import kotlinx.parcelize.Parcelize

@Parcelize
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
    var attributes: MutableList<ProductAttribute> = mutableListOf()
): Parcelable {
    fun getPriceStr(): String {
        return price?.formatCurrency() ?: ""
    }
}

@Parcelize
data class ProductAttribute(
    var id: Long? = null,
    var multiple: Boolean = false,
    var required: Boolean = false,
    var productId: Long? = null,
    var name: String? = null,
    var options: MutableList<ProductAttributeOption> = mutableListOf()
): Parcelable {
    fun getOptionsStr(): String {
        var result = options.fold("") {acc, item -> "$acc${item.name} --- ${item.getPriceStr()}\n"}
        return result.dropLast(1)
    }
}

@Parcelize
data class ProductAttributeOption(
    var id: Long? = null,
    var name: String? = null,
    var price: Double? = null,
    var productAttributeId: Long? = null
): Parcelable {
    fun getPriceStr(): String = price?.formatCurrency() ?: ""
}
