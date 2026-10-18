package com.example.s8162795assignment2.di

import com.example.s8162795assignment2.data.repository.AuthRepositoryImpl
import com.example.s8162795assignment2.data.repository.DashboardRepositoryImpl
import com.example.s8162795assignment2.domain.repository.AuthRepository
import com.example.s8162795assignment2.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindAuth(implementation: AuthRepositoryImpl): AuthRepository
    @Binds @Singleton abstract fun bindDashboard(implementation: DashboardRepositoryImpl): DashboardRepository
}
