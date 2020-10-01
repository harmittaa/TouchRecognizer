package com.github.harmittaa.touchobserver.repository

import com.github.harmittaa.touchobserver.model.TouchGesture
import com.github.harmittaa.touchobserver.remote.AuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class TouchRepository(val auth: AuthProvider, val firebaseDatabase: FirebaseDatabase) {

    fun storeGesture(gesture: TouchGesture) {
        val userId = auth.userId
        val dbRefToUser = firebaseDatabase.reference.child("data").child(userId!!)
        Timber.d("Storing data with user ID $userId")
        storeGesture(gesture, dbRefToUser)
    }

    private fun storeGesture(
        gesture: TouchGesture,
        refToUser: DatabaseReference
    ) {
        val ref = refToUser.child(gesture.gestureDirection.toString()).push()
        Timber.d("Storing gesture $gesture")
        try {
            ref.setValue(gesture)
            val touchPointRef = ref.child("touchPoints").push()
            gesture.touchPoints.forEach { point -> touchPointRef.setValue(point) }
        } catch (e: DatabaseException) {
            FirebaseCrashlytics.getInstance().recordException(TouchObserverThrowable("Exception in storeGesture", e))
            Timber.d("CRAAASH $e for object $gesture")
        }
    }
}
