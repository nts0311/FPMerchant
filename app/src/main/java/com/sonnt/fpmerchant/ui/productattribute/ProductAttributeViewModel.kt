package com.sonnt.fpmerchant.ui.productattribute

import com.sonnt.fpmerchant.model.ProductAttribute
import com.sonnt.fpmerchant.model.ProductAttributeOption
import com.sonnt.fpmerchant.ui.base.BaseViewModel

class ProductAttributeViewModel : BaseViewModel() {
    var productAttribute = ProductAttribute(multiple = false, required = false)

    fun addOptions(option: ProductAttributeOption) {
        productAttribute.options.add(option)
    }

    fun deleteOption(pos: Int, option: ProductAttributeOption) {
        productAttribute.options.removeAt(pos)
    }

    fun validateInput(): Boolean {
        if (productAttribute.name.isNullOrEmpty()) {
            error("Tên không hợp lệ")
            return false
        }

        return true
    }

}