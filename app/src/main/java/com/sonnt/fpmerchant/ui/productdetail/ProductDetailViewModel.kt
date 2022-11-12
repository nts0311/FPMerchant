package com.sonnt.fpmerchant.ui.productdetail

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.sonnt.fpmerchant.FpMerchantApplication
import com.sonnt.fpmerchant.data.repos.MenuRepo
import com.sonnt.fpmerchant.data.repos.ProductRepository
import com.sonnt.fpmerchant.model.*
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.network.dto.request.ChangeMerchantActivityStatusRequest
import com.sonnt.fpmerchant.ui._base.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class ProductDetailViewModel : BaseViewModel() {
    var menuId: Long = -1
    var product = Product(status = "AVAILABLE")
        set(value) {
            field = value
            onSetProduct()
        }

    val menus: List<ProductMenu>
        get() = MenuRepo.shared.menus

    val isCreatingNewProduct: Boolean
        get() = product.id == null

    var isUploadingImage = MutableLiveData(false)
    var categories = MutableLiveData<List<ProductCategory>>()

    val result = MutableLiveData<Boolean>()

    init {
        try {
            MediaManager.init(FpMerchantApplication.instance, mapOf("cloud_name" to "dgksrtctm"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onSetProduct() {

    }

    fun onAddOrEditAttribute(attribute: ProductAttribute) {

        val index = product.attributes.indexOfFirst { it == attribute }
        if (index == -1) {
            product.attributes.add(attribute)
        } else {
            product.attributes[index] = attribute
        }
    }

    fun deleteAttribute(pos: Int) {
        product.attributes.removeAt(pos)
    }

    fun validateInput(): Boolean {
        if (product.name.isNullOrEmpty()) {
            error("Tên sản phẩm không hợp lệ")
            return false
        }

        if (product.price == null || product.price == 0.0) {
            error("Giá không hợp lệ")
            return false
        }

        if (product.imageUrl.isNullOrEmpty()) {
            error("Ảnh sản phẩm không hợp lệ")
            return false
        }

        if (product.categoryId == null) {
            error("Chọn loại sản phẩm")
            return false
        }

        return true
    }

    fun getCategory() {
        viewModelScope.launch {
            val response = callApi { NetworkModule.productService.getProductCategories() }

            when (response) {
                is ApiResult.Success -> {
                    categories.value = response.data?.categories ?: listOf()
                }

                is ApiResult.Failed -> {
                    error("Lỗi khi lấy danh sách loại đồ ăn")
                }
            }
        }
    }

    fun setCategory(position: Int) {
        val category = categories.value?.get(position) ?: return

        if (category.id == product.categoryId) {
            return
        }

        product.categoryId = category.id
    }

    fun setMenu(adapterPos: Int) {
        val menu = MenuRepo.shared.menus[adapterPos]

        if (menu.id == product.tagId) {
            return
        }

        product.tagId = menu.id
    }

    fun getCategoriesPos(): Int {
        return categories.value?.indexOfFirst { it.id == product.categoryId } ?: -1
    }

    fun getMenuPos(): Int {
        return menus.indexOfFirst { it.id == product.tagId }
    }

    fun setProductAvailability(availableNow: Boolean) {
        product.status = if(availableNow) "AVAILABLE" else "OUT_OF_STOCK"
    }

    fun uploadProductImage(uri: Uri) {
        MediaManager.get()
            .upload(uri)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    isUploadingImage.value = true
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                }

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    product.imageUrl = resultData?.get("url") as? String?
                    isUploadingImage.value = false
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    this@ProductDetailViewModel.error("Uppload ảnh thất bại")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                }

            })
            .unsigned("kypwklls")
            .dispatch(FpMerchantApplication.instance)
    }

    fun saveProduct() {
        viewModelScope.launch {
            if (isCreatingNewProduct) {

            } else {
                val isSuccess = ProductRepository.shared.editProduct(product)
                if (isSuccess) {
                    result.value = true
                } else {
                    error("Lỗi khi lưu sản phẩm.")
                }
            }
        }
    }

}