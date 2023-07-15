package com.example.inventory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InventoryViewModel(private val itemDao: ItemDao): ViewModel(){

    val items: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if(itemName.isBlank() or itemPrice.isBlank() or itemCount.isBlank()) return false
        return true
    }

    private fun getNewItem(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(itemName = itemName, itemPrice = itemPrice.toDouble(), quantityInStock = itemCount.toInt())
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        insertItem(getNewItem(itemName, itemPrice, itemCount))
    }

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    fun updateItem(itemId: Int, itemName: String, itemPrice: String, itemCount: String) {
        val updatedItem = getUpdatedItem(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

    private fun getUpdatedItem(itemId: Int, itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(id = itemId, itemName = itemName, itemPrice = itemPrice.toDouble(), quantityInStock = itemCount.toInt())
    }

    fun sellItem(item: Item) {
        if(item.quantityInStock > 0) {
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    fun isStockAvailable(item: Item): Boolean { return (item.quantityInStock > 0) }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

}

class InventoryViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}