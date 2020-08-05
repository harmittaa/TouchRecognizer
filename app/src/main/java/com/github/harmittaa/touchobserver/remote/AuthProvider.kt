package com.github.harmittaa.touchobserver.remote

import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

interface AuthProvider {
    var userId: String?
}

class AuthProviderImpl(private val firebaseAuth: FirebaseAuth) : AuthProvider {
    override var userId: String? = null

    init {
        if (firebaseAuth.currentUser != null) {
            userId = firebaseAuth.currentUser?.uid
        } else {
            signInUserAuth()
        }
    }

    private fun signInUserAuth() {
        firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseAuth.currentUser?.let {
                    userId = it.uid
                }
            } else {
                Timber.d("ALWAYS SOMETHING")
                // TODO unfuck
            }
        }
    }
}