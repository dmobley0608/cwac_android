package com.dmobley0608.cwac.data.repos

import android.util.Log
import com.dmobley0608.cwac.data.model.InventoryItem
import com.dmobley0608.cwac.data.model.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class InventoryRepo(private val firestore: FirebaseFirestore) {
    private val Tag = "Inventory Repo"
    private val inventoryRef = firestore.collection("inventory")


    //Get Inventory Items
   fun fetchInventoryItems(): Flow<List<InventoryItem>> =
            callbackFlow {
                val subscription = inventoryRef.orderBy("name").addSnapshotListener { snapshot, _ ->
                    snapshot?.let {
                        trySend(it.map { doc ->
                            doc.toObject(InventoryItem::class.java).copy(key=doc.id)
                        }).isSuccess
                    }
                }
                awaitClose{subscription.remove()}
            }



    //Create Inventory Item
    suspend fun addInventoryItem(inventoryItem: InventoryItem): Result<String> =
        try {
            Log.d(Tag, "Creating New Inventory Item")
            val docRef = inventoryRef.document()
            val key = docRef.id
            inventoryItem.key = key
            docRef.set(inventoryItem).await()
            Log.d(Tag, "Inventory Item created successfully")
            Result.Success(key)
        } catch (e: Exception) {
            Log.e(Tag, "Error creating inventory item")
            Result.Error(e)
        }

    //Edit Inventory Item
    suspend fun editInventoryItem(key:String,inventoryItem: InventoryItem): Result<String> =
        try {
            Log.d(Tag, "Editng Inventory Item with key: $key")
            val docRef = inventoryRef.document(key)
            docRef.set(inventoryItem).await()
            Log.d(Tag, "Inventory Item edited successfully")
            Result.Success(inventoryItem.key!!)
        } catch (e: Exception) {
            Log.e(Tag, "Error editing inventory item",e)
            Result.Error(e)
        }
}