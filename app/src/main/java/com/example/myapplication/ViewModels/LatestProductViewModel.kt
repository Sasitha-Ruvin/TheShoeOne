package com.example.myapplication.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Network.RetrofitInstance
import com.example.myapplication.models.LatestProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LatestProductsViewModel: ViewModel() {
    private val _latestProducts = MutableStateFlow<List<LatestProduct>>(emptyList())
    val latestProducts: StateFlow<List<LatestProduct>> = _latestProducts

    init{
        fetchLatestProducts()
    }

    private fun fetchLatestProducts(){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getLatestProducts()
                _latestProducts.value = response
            }catch(e:Exception){
                e.printStackTrace()
            }
        }
    }
}