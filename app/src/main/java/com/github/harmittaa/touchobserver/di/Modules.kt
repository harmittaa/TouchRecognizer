package com.github.harmittaa.touchobserver.di

import com.github.harmittaa.touchobserver.logic.GestureProcessor
import com.github.harmittaa.touchobserver.screens.swipe.SwipeViewModel
import com.github.harmittaa.touchobserver.remote.AuthProvider
import com.github.harmittaa.touchobserver.remote.AuthProviderImpl
import com.github.harmittaa.touchobserver.repository.TouchRepository
import com.github.harmittaa.touchobserver.repository.UserRepository
import com.github.harmittaa.touchobserver.screens.onboarding.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val viewModelModule = module {
    factory { SwipeViewModel(repository = get(), gestureProcessor = get()) }
    factory { OnboardingViewModel(get()) }
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
    factory { UserRepository(auth = get(), firebaseDatabase = get()) }
}

val logicModule = module {
    factory { GestureProcessor() }
}