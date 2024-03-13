package com.dmobley0608.cwac.ui.views.inventory

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.InventoryItem
import com.dmobley0608.cwac.data.repos.InventoryRepo
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import kotlinx.coroutines.launch

class InventoryViewModel: ViewModel() {
    private val Tag = "Inventory view model"

    private val _isLoading = mutableStateOf(false)
    val isLoading:Boolean
        get() = _isLoading.value

    //Inventory List
    private val _inventoryList=MutableLiveData<List<InventoryItem>>()
    val inventoryList:LiveData<List<InventoryItem>>
        get() = _inventoryList

    //Repo
    private val inventoryRepo:InventoryRepo

    init{
        Log.d(Tag, "Initializing Inventory List")
        inventoryRepo = InventoryRepo(FirestoreInjection.instance())
        loadInventoryItems()
    }

    //Get Inventory items
    fun loadInventoryItems(){
        _isLoading.value = true
        viewModelScope.launch {
            inventoryRepo.fetchInventoryItems().collect {
                _inventoryList.value = it
            }
            Log.d(Tag, "$inventoryList")
        }
        _isLoading.value = false
    }

    //Edit Inventory Item
    fun editInventoryItem(key:String,inventoryItem: InventoryItem){
        viewModelScope.launch {
            inventoryRepo.editInventoryItem(key,inventoryItem)
        }
    }
}