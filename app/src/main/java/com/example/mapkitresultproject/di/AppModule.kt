package com.example.mapkitresultproject.di

import android.content.Context
import com.example.mapkitresultproject.data.repository.MapKitCreateRoutRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitInteractionMapRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitSearchRepositoryImpl
import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.example.mapkitresultproject.domain.repository.MapKitInteractionMapRepository
import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext appContext: Context): Context = appContext

    @Provides
    @Singleton
    fun providesMapKitSearchRepository(appContext: Context): MapKitSearchRepository =
        MapKitSearchRepositoryImpl(context = appContext)

    @Provides
    @Singleton
    fun providesMapKitCreateRoutRepository(appContext: Context): MapKitCreateRoutRepository =
        MapKitCreateRoutRepositoryImpl(context = appContext)

    @Provides
    @Singleton
    fun providesMapKitInteractionMapRepository(appContext: Context): MapKitInteractionMapRepository =
        MapKitInteractionMapRepositoryImpl(context = appContext)

}