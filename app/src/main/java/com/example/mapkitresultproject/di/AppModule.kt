package com.example.mapkitresultproject.di

import com.example.mapkitresultproject.data.repository.MapKitCreateRoutRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitSearchRepositoryImpl
import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providesMapKitSearchRepository(): MapKitSearchRepository = MapKitSearchRepositoryImpl()

    @Provides
    @Singleton
    fun providesMapKitCreateRoutRepository(): MapKitCreateRoutRepository = MapKitCreateRoutRepositoryImpl()

}