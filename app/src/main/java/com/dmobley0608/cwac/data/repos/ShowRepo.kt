package com.dmobley0608.cwac.data.repos

import android.util.Log
import com.dmobley0608.cwac.data.model.Message
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.Show
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ShowRepo(private val firestore: FirebaseFirestore) {
    private val TAG = "SHOW REPO"
    private val shows: String = "shows"
    private val notesCollection: String = "notes"


    //Create New SHOW
    suspend fun createShow(show: Show): Result<String> =
        try {
            Log.d(TAG, "CREATING SHOW")
            val docRef = firestore.collection(shows).document()
            show.key = docRef.id
            firestore.collection(shows).document(docRef.id).set(show).await()
            Log.d(TAG, "SHOW CREATED SUCCESSFULLY")
            Result.Success(show.key!!)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR CREATING SHOW", e)
            Result.Error(e)
        }

    //Fetch All Shows
    suspend fun getShows(): Flow<List<Show>> =
        callbackFlow {
            Log.d(TAG, "FETCHING SHOWS")
            val subscription =
                firestore.collection(shows).orderBy("arrivalDate", Query.Direction.ASCENDING)
                    .addSnapshotListener { snapshot, _ ->
                        snapshot?.let {
                            trySend(it.documents.map { doc ->
                                doc.toObject(Show::class.java)!!.copy()
                            }).isSuccess
                        }
                    }
            awaitClose { subscription.remove() }
            Log.d(TAG, "SHOWS SUCCESSFULLY FETCHED")
        }

    //GET SHOW BY KEY
    suspend fun fetchShowByKey(key: String): Result<Show> =
        try {
            val showRef = firestore.collection(shows).document(key).get().await()
            val show = showRef.toObject(Show::class.java)!!.copy()
            Result.Success(show)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR FETCH SHOW BY ID: $key", e)
            Result.Error(e)
        }

    //Edit Show
    suspend fun editShow(show: Show): Result<Show> =
        try {
            Log.d(TAG, "EDITING SHOW")
            if (show.key != null) {
                firestore.collection(shows).document(show.key!!).set(show).await()
            }
            Result.Success(show)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR EDITING SHOW", e)
            Result.Error(e)
        }

    // Fetch All Messages
    fun getShowMessages(showId: String): Flow<List<Message>> =
        callbackFlow {
            val subscription = firestore.collection(shows)
                .document(showId).collection(notesCollection)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { snapShot, _ ->
                    snapShot?.let {
                        trySend(it.documents.map { doc ->
                            doc.toObject(Message::class.java)!!.copy()
                        }).isSuccess
                    }
                }
            awaitClose { subscription.remove() }
        }

    // Add Message To Show
    suspend fun sendMessage(showId: String, message: Message): Result<Unit> =
        try {
            Log.d(TAG, "SENDING MESSAGE")
            firestore.collection(shows)
                .document(showId).collection(notesCollection).add(message).await()
            Log.d(TAG, "MESSAGE SENT SUCCESSFULLY")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR SENDING MESSAGE", e)
            Result.Error(e)
        }

}
