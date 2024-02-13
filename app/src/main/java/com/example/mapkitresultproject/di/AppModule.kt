package com.example.mapkitresultproject.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mapkitresultproject.data.local.ConsigneeDao
import com.example.mapkitresultproject.data.local.Database
import com.example.mapkitresultproject.data.local.ShipperDao
import com.example.mapkitresultproject.data.remote.RoutesApi
import com.example.mapkitresultproject.data.remote.utils.Constants.BASE_URL
import com.example.mapkitresultproject.data.repository.ConsigneeRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitCreateRoutRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitInteractionMapRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitSearchRepositoryImpl
import com.example.mapkitresultproject.data.repository.ShipperRepositoryImpl
import com.example.mapkitresultproject.domain.Constants.DATABASE_NAME
import com.example.mapkitresultproject.domain.repository.ConsigneeRepository
import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.example.mapkitresultproject.domain.repository.MapKitInteractionMapRepository
import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import com.example.mapkitresultproject.domain.repository.ShipperRepository
import com.example.mapkitresultproject.domain.usecase.ConsigneeUsesCases
import com.example.mapkitresultproject.domain.usecase.ShipperUsesCases
import com.example.mapkitresultproject.domain.usecase.consignee.DeleteConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.GetAllConsigneesUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.GetConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.InsertConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.DeleteShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.GetAllShippersUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.GetShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.InsertShipperUseCase
import dagger.Component.Factory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): Database {
        return Room.databaseBuilder(
            context = application,
            klass = Database::class.java,
            name = DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideShipperDao(
        database: Database
    ): ShipperDao = database.shipperDao

    @Provides
    @Singleton
    fun provideConsigneeDao(
        database: Database
    ): ConsigneeDao = database.consigneeDao

    @Provides
    @Singleton
    fun providesShipperRepository(
        shipperDao: ShipperDao
    ): ShipperRepository = ShipperRepositoryImpl(shipperDao = shipperDao)

    @Provides
    @Singleton
    fun providesConsigneeRepository(
        consigneeDao: ConsigneeDao
    ): ConsigneeRepository = ConsigneeRepositoryImpl(consigneeDao = consigneeDao)

    @Provides
    @Singleton
    fun providesShipperUsesCases(
        deleteShipperUseCase: DeleteShipperUseCase,
        insertShipperUseCase: InsertShipperUseCase,
        getAllShipperUseCase: GetAllShippersUseCase,
        getShipperUseCase: GetShipperUseCase
    ): ShipperUsesCases = ShipperUsesCases(
        deleteShipperUseCase,insertShipperUseCase,getAllShipperUseCase,getShipperUseCase)
    @Provides
    @Singleton
    fun providesConsigneeUsesCases(
        deleteConsigneeUseCase: DeleteConsigneeUseCase,
        insertConsigneeUseCase: InsertConsigneeUseCase,
        getAllConsigneeUseCase: GetAllConsigneesUseCase,
        getConsigneeUseCase: GetConsigneeUseCase
    ): ConsigneeUsesCases = ConsigneeUsesCases(
        deleteConsigneeUseCase,insertConsigneeUseCase,getAllConsigneeUseCase,getConsigneeUseCase)

    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext appContext: Context): Context = appContext

    @Provides
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

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesPostService(retrofit: Retrofit) = retrofit.create(RoutesApi::class.java)

}