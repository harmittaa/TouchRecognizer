package com.github.harmittaa.touchobserver.di

import com.github.harmittaa.touchobserver.SwipeViewModel
import com.github.harmittaa.touchobserver.remote.AuthProvider
import com.github.harmittaa.touchobserver.remote.AuthProviderImpl
import com.github.harmittaa.touchobserver.repository.TouchRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val viewModelModule = module {
    factory { SwipeViewModel(get()) }
}

val firebaseModule = module {
    single { FirebaseDatabase.getInstance() }
    single { FirebaseAuth.getInstance() }
}

val authModule = module {
    single<AuthProvider> { AuthProviderImpl(firebaseAuth = get()) }
}

val repositoryModule = module {
    factory { TouchRepository(auth = get(), firebaseDatabase = get()) }
}