package com.github.harmittaa.touchobserver.repository

import com.github.harmittaa.touchobserver.screens.swipe.GestureDirection
import com.github.harmittaa.touchobserver.model.TouchGesture
import com.github.harmittaa.touchobserver.remote.AuthProvider
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

sealed class Resource {
    object Success : Resource()
    class Failure(val reason: String) : Resource()
}

suspend fun DatabaseReference.getSnapshotValue(): DataSnapshot {
    return withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            addListenerForSingleValueEvent(FValueEventListener(
                onDataChange = { continuation.resume(it) },
                onError = { continuation.resumeWithException(it.toException()) }
            ))
        }
    }
}

class FValueEventListener(
    val onDataChange: (DataSnapshot) -> Unit,
    val onError: (DatabaseError) -> Unit
) : ValueEventListener {
    override fun onDataChange(data: DataSnapshot) = onDataChange.invoke(data)
    override fun onCancelled(error: DatabaseError) = onError.invoke(error)
}


class UserRepository(val auth: AuthProvider, val firebaseDatabase: FirebaseDatabase) {

    suspend fun attemptSignIn(): Resource {
        return auth.attemptSignIn()
    }

    suspend fun instantiateData(): Resource {
        val id = auth.userId ?: return Resource.Failure("Anonymous user ID not found")
        val dbRefToUser = firebaseDatabase.reference.child("data").child(id)
        return try {
            val result = dbRefToUser.getSnapshotValue()
            Resource.Success
        } catch(e: Exception)  {
            Timber.d("Something went wrong $e")
            Resource.Failure(reason = e.localizedMessage ?: "Unknown error")
        }
    }
}

class TouchRepository(val auth: AuthProvider, val firebaseDatabase: FirebaseDatabase) {

    fun storeGesture(gesture: TouchGesture) {
        val userId = auth.userId
        val dbRefToUser = firebaseDatabase.reference.child("data").child(userId!!)
        Timber.d("Storing data with user ID $userId")
        dbRefToUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    storeGesture(gesture, dbRefToUser)
                } else {
                    firebaseDatabase.reference.child("data").child(userId)
                        .setValue(gesture.gestureDirection.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                storeGesture(gesture, dbRefToUser)
                            }
                        }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun storeSwipes(type: GestureDirection, allTouchGestures: List<TouchGesture>) {
        Timber.d("Storing ${allTouchGestures.count()} values")
        val userId = auth.userId
        val dbRefToUser = firebaseDatabase.reference.child("data").child(userId!!)
        dbRefToUser.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    storeGestures(allTouchGestures, dbRefToUser, type)
                } else {
                    firebaseDatabase.reference.child("data").child(userId)
                        .setValue(type.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                storeGestures(allTouchGestures, dbRefToUser, type)
                            }
                        }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun storeGestures(
        gestures: List<TouchGesture>,
        refToUser: DatabaseReference,
        type: GestureDirection
    ) {
        gestures.forEach { gesture ->
            val ref = refToUser.child(type.toString()).push()
            Timber.d("Storing gesture $gesture")
            try {
                ref.setValue(gesture)
            } catch (e: DatabaseException) {
                Timber.d("CRAAASH $e for object $gesture")
            }
            val touchPointRef = ref.child("touchPoints").push()
            gesture.touchPoints.forEach { point -> touchPointRef.setValue(point) }
        }
    }

    private fun storeGesture(
        gesture: TouchGesture,
        refToUser: DatabaseReference
    ) {
        val ref = refToUser.child(gesture.gestureDirection.toString()).push()
        Timber.d("Storing gesture $gesture")
        try {
            ref.setValue(gesture)
        } catch (e: DatabaseException) {
            Timber.d("CRAAASH $e for object $gesture")
        }
        val touchPointRef = ref.child("touchPoints").push()
        gesture.touchPoints.forEach { point -> touchPointRef.setValue(point) }
    }
}