package com.github.harmittaa.touchobserver.repository

import com.github.harmittaa.touchobserver.model.DataRemoval
import com.github.harmittaa.touchobserver.model.UserData
import com.github.harmittaa.touchobserver.remote.AuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
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

    suspend fun instantiateData(
        gender: UserData.Gender,
        handedness: UserData.Handedness
    ): Resource {
        val id = auth.userId ?: return Resource.Failure("Anonymous user ID not found")
        val dbRefToUser = firebaseDatabase.reference.child("data").child(id)
        return try {
            dbRefToUser.getSnapshotValue()
            dbRefToUser.setValue(UserData(gender, handedness))
            localDataStore.storeConsent()
            Resource.Success
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance()
                .recordException(TouchObserverThrowable("Exception in instantiateData", e))
            Timber.d("Something went wrong $e")
            Resource.Failure(reason = e.localizedMessage ?: "Unknown error")
        }
    }

    suspend fun onDataRemovalClicked(): Resource {
        val id = auth.userId ?: return Resource.Failure("Anonymous user ID not found")
        val dbRefToUser = firebaseDatabase.reference.child("data").child(id)
        return try {
            dbRefToUser.child("Removal").setValue(DataRemoval())
            Resource.Success
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance()
                .recordException(TouchObserverThrowable("Exception in onDataRemovalClicked", e))
            Timber.d("Something went wrong $e")
            Resource.Failure(reason = e.localizedMessage ?: "Unknown error")
        }
    }
}

class TouchObserverThrowable(information: String, exception: Exception) :
    Throwable(message = "$information || Original cause ${exception.message}", cause = exception)
