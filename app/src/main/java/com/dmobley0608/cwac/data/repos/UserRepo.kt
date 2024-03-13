package com.dmobley0608.cwac.data.repos

import android.util.Log
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class UserRepo(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {
    val TAG = "USER REPO"

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        role: String
    ): Result<User> =
        try {
            Log.d(TAG, "CREATING USER")
            auth.createUserWithEmailAndPassword(email, password).await()
            val user =  User(
                key = auth.uid ?: "No Key",
                firstname = firstName,
                lastName = lastName,
                role = role,
                email = email,
            )
            saveUserToFireStore(user)
            Log.d(TAG, "USER CREATED SUCCESSFULLY")
            Result.Success(user)

        } catch (e: Exception) {
            Log.e(TAG, "ERROR CREATING USER", e)
            Result.Error(e)
        }

    private suspend fun saveUserToFireStore(user: User) {
        firestore.collection("users").document(user.key.toString()).set(user).await()
    }

    suspend fun signIn(email: String, password: String): Result<Boolean> =
        try {
            Log.d(TAG, "USER SIGNING IN")
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "USER SIGNED IN SUCCESSFULLY")
            Result.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR LOGGING IN", e)
            Result.Error(e)
        }

    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            Log.i(TAG, "FETCHING CURRENT USER WITH ID: $uid")
            val userDocument = firestore.collection("users").document(uid).get().await()
            val user = userDocument.toObject(User::class.java)
            if (user != null) {
                Log.d(TAG,"CURRENT USER: $uid")
                Result.Success(user)
            } else {
                Log.e(TAG, "NOT FOUND")
                Result.Error(Exception("User data not found"))
            }
        } else {
            Log.e(TAG, "USER NOT AUTHENTICATED")
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Log.e(TAG, "ERROR FETCHING USER", e)
        Result.Error(e)
    }

    suspend fun getAllUsers():List<User> =
        try{
            val result = firestore.collection("users").get().await()
            result.documents.map { user-> user.toObject(User::class.java)!! }
        }catch(e:Exception){
            Log.e(TAG, "Error Fetchin users", e)
            emptyList<User>()
        }

}
