package com.example.c36b.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.c36b.model.ProductModel
import com.example.c36b.repository.ProductRepository

class ProductViewModel(val repo: ProductRepository) : ViewModel() {

    fun addProduct(
        productModel: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addProduct(productModel, callback)
    }

    private val _products = MutableLiveData<ProductModel?>()
    val products: LiveData<ProductModel?> get() = _products

    fun getProductById(
        productID: String,
    ) {
        repo.getProductById(productID) { success, message, value ->
            if (success) {
                _products.postValue(value)
            } else {
                _products.postValue(null)
            }
        }
    }

    private val _allProducts = MutableLiveData<List<ProductModel?>>()
    val allProducts: LiveData<List<ProductModel?>> get() = _allProducts

    private var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = loading

    fun getAllProduct() {
        _loading.postValue(true)
        repo.getAllProduct { success, message, value ->
            if (success) {
                _loading.postValue(false)
                _allProducts.postValue(value)
            } else {
                _loading.postValue(false)
                _allProducts.postValue(emptyList())
            }
        }
    }

    fun updateProduct(
        productId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateProduct(productId, data, callback)
    }

    fun deleteProduct(
        productId: String, callback: (Boolean, String) -> Unit
    ) {
        repo.deleteProduct(productId, callback)
    }
}