package com.dmobley0608.cwac.data.repos

import android.util.Log
import com.dmobley0608.cwac.data.model.Message
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.WorkOrder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WorkOrderRepo(private val firestore: FirebaseFirestore) {
    private val TAG = "WORK ORDER REPO"
    private val workOrderCollection: String = "work-orders"
    private val messageCollection: String = "messages"


    //Create New Work Order
    suspend fun createWorkOrder(workOrder: WorkOrder): Result<String> =
        try {
            Log.d(TAG, "CREATING WORK ORDER")
            val docRef = firestore.collection(workOrderCollection).document()
            workOrder.id = docRef.id
            firestore.collection(workOrderCollection).document(docRef.id).set(workOrder).await()
            Log.d(TAG, "WORK ORDER CREATED SUCCESSFULLY")
            Result.Success(workOrder.id!!)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR CREATING WORK ORDER", e)
            Result.Error(e)
        }

    //Fetch All Work Orders
    suspend fun getWorkOrders(): Flow<List<WorkOrder>> =
        callbackFlow {
            Log.d(TAG, "FETCHING WORK ORDERs")
            val subscription =
                firestore.collection(workOrderCollection).orderBy("priority",Query.Direction.DESCENDING).addSnapshotListener { snapshot, _ ->
                    snapshot?.let {
                        trySend(it.documents.map { doc ->
                            doc.toObject(WorkOrder::class.java)!!.copy()
                        }).isSuccess
                    }
                }
            awaitClose { subscription.remove() }
            Log.d(TAG, "WORK ORDERS SUCCESSFULLY FETCHED")
        }


    suspend fun fetchWorkOrderByKey(key: String): Result<WorkOrder> =
        try {
            val workOrderRef = firestore.collection(workOrderCollection).document(key).get().await()
            val workOrder = workOrderRef.toObject(WorkOrder::class.java)!!.copy()
            Result.Success(workOrder)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR CREATING WORK ORDER", e)
            Result.Error(e)
        }

    //Edit Work Order
    suspend fun editWorkOrder(workOrder: WorkOrder): Result<WorkOrder> =
        try{
            Log.d(TAG, "EDITING WORK ORDER")
            if (workOrder.id != null) {
                firestore.collection(workOrderCollection).document(workOrder.id!!).set(workOrder).await()
            }
            Result.Success(workOrder)
        }catch (e: Exception) {
            Log.e(TAG, "ERROR EDITING WORK ORDER", e)
            Result.Error(e)
        }

    // Fetch All Messages
    fun getWorkOrderMessages(workOrderId: String): Flow<List<Message>> =
        callbackFlow {
            val subscription = firestore.collection(workOrderCollection)
                .document(workOrderId).collection(messageCollection).orderBy("date")
                .addSnapshotListener { snapShot, _ ->
                    snapShot?.let {
                        trySend(it.documents.map { doc ->
                            doc.toObject(Message::class.java)!!.copy()
                        }).isSuccess
                    }
                }
            awaitClose { subscription.remove() }
        }

    // Add Message To Work Order
    suspend fun sendMessage(workOrderId: String, message: Message): Result<Unit> =
        try {
            Log.d(TAG, "SENDING MESSAGE")
            firestore.collection(workOrderCollection)
                .document(workOrderId).collection(messageCollection).add(message).await()
            Log.d(TAG, "MESSAGE SENT SUCCESSFULLY")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR SENDING MESSAGE", e)
            Result.Error(e)
        }

}

