package com.github.harmittaa.touchobserver.repository

import com.github.harmittaa.touchobserver.remote.AuthProvider
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class UserRepository(
    private val auth: AuthProvider,
    private val firebaseDatabase: FirebaseDatabase,
    private val localDataStore: LocalDataStore
) {
    suspend fun hasGivenConsent() =
        localDataStore.hasGivenConsent()

    suspend fun attemptSignIn(): Resource {
        return auth.attemptSignIn()
    }

    fun userReady() = auth.isFirebaseUserAvailable()

    suspend fun instantiateData(): Resource {
        val id = auth.userId ?: return Resource.Failure("Anonymous user ID not found")
        val dbRefToUser = firebaseDatabase.reference.child("data").child(id)
        return try {
            dbRefToUser.getSnapshotValue()
            localDataStore.storeConsent()
            Resource.Success
        } catch (e: Exception) {
            Timber.d("Something went wrong $e")
            Resource.Failure(reason = e.localizedMessage ?: "Unknown error")
        }
    }
}