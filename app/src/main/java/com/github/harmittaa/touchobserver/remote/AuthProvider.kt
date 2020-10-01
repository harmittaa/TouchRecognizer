package com.github.harmittaa.touchobserver.remote

import com.github.harmittaa.touchobserver.repository.Resource
import com.github.harmittaa.touchobserver.repository.TouchObserverThrowable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.lang.Exception
import kotlinx.coroutines.tasks.await

interface AuthProvider {
    var userId: String?
    suspend fun attemptSignIn(): Resource
    fun isFirebaseUserAvailable(): Boolean
}

class AuthProviderImpl(private val firebaseAuth: FirebaseAuth) : AuthProvider {
    override var userId: String? = null

    override suspend fun attemptSignIn(): Resource {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            userId = user.uid
            Resource.Success
        } else {
            val result = signInUserAuth()
            if (result.first) {
                userId = result.second
                Resource.Success
            } else {
                Resource.Failure(reason = result.second)
            }
        }
    }

    override fun isFirebaseUserAvailable(): Boolean {
        userId = firebaseAuth.currentUser?.uid
        return userId != null
    }

    private suspend fun signInUserAuth(): Pair<Boolean, String> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            val user = result.user
            return if (user != null) {
                Pair(true, user.uid)
            } else {
                Pair(false, "User is null, please try again")
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(TouchObserverThrowable("Exception in signInUserAuth", e))
            Pair(false, "Could not create anonymous user ${e.localizedMessage}")
        }
    }
}
