package com.sonnt.fpmerchant.ui.productdetail

import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.model.ProductAttribute
import com.sonnt.fpmerchant.model.ProductAttributeOption
import com.sonnt.fpmerchant.ui._base.BaseViewModel

class ProductDetailViewModel : BaseViewModel() {
    var product = Product()

    fun onAddOrEditAttribute(attribute: ProductAttribute) {
        if (attribute.id == null) {
            val index = product.attributes.indexOfFirst { it == attribute }
            if (index == -1) {
                product.attributes.add(attribute)
            } else {
                product.attributes[index] = attribute
            }

        } else {
            val index = product.attributes.indexOfFirst { it.id == attribute.id }
            if(index != -1) {
                product.attributes[index] = attribute
            }
        }
    }

    fun deleteAttribute(pos: Int) {
        product.attributes.removeAt(pos)
    }

}