package com.example.mapkitresultproject.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mapkitresultproject.data.local.room.ConsigneeDao
import com.example.mapkitresultproject.data.local.room.Database
import com.example.mapkitresultproject.data.local.room.ShipperDao
import com.example.mapkitresultproject.data.local.room.TransportDao
import com.example.mapkitresultproject.data.local.sharedPref.SharedPrefUserStorage
import com.example.mapkitresultproject.data.remote.FirebaseService
import com.example.mapkitresultproject.data.remote.RoutesApi
import com.example.mapkitresultproject.data.remote.utils.Constants.BASE_URL
import com.example.mapkitresultproject.data.repository.AuthRepositoryImpl
import com.example.mapkitresultproject.data.repository.ConsigneeRepositoryImpl
import com.example.mapkitresultproject.data.repository.FirebaseServiceImpl
import com.example.mapkitresultproject.data.repository.MapKitCreateRoutRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitInteractionMapRepositoryImpl
import com.example.mapkitresultproject.data.repository.MapKitSearchRepositoryImpl
import com.example.mapkitresultproject.data.repository.SharedPrefUserStorageRepositoryImpl
import com.example.mapkitresultproject.data.repository.ShipperRepositoryImpl
import com.example.mapkitresultproject.data.repository.TransportRepositoryImpl
import com.example.mapkitresultproject.domain.Constants.DATABASE_NAME
import com.example.mapkitresultproject.domain.repository.AuthRepository
import com.example.mapkitresultproject.domain.repository.ConsigneeRepository
import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.example.mapkitresultproject.domain.repository.MapKitInteractionMapRepository
import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import com.example.mapkitresultproject.domain.repository.SharedPrefUserStorageRepository
import com.example.mapkitresultproject.domain.repository.ShipperRepository
import com.example.mapkitresultproject.domain.repository.TransportRepository
import com.example.mapkitresultproject.domain.usecase.ConsigneeUsesCases
import com.example.mapkitresultproject.domain.usecase.SharedPrefUserStorageUsesCases
import com.example.mapkitresultproject.domain.usecase.ShipperUsesCases
import com.example.mapkitresultproject.domain.usecase.consignee.DeleteConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.GetAllConsigneesUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.GetConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.InsertConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.UpdateConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.DeleteUIDUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.GetUIDUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.SaveUIDUsesCase
import com.example.mapkitresultproject.domain.usecase.shipper.DeleteShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.GetAllShippersUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.GetShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.InsertShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.UpdateShipperUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
    fun provideTransportDao(
        database: Database
    ): TransportDao = database.transportDao

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
    fun providesTransportRepository(
        transportDao: TransportDao
    ): TransportRepository = TransportRepositoryImpl(transportDao = transportDao)

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
        getShipperUseCase: GetShipperUseCase,
        updateShipperUseCase: UpdateShipperUseCase
    ): ShipperUsesCases = ShipperUsesCases(
        deleteShipperUseCase,
        insertShipperUseCase,
        getAllShipperUseCase,
        getShipperUseCase,
        updateShipperUseCase
    )

    @Provides
    @Singleton
    fun providesConsigneeUsesCases(
        deleteConsigneeUseCase: DeleteConsigneeUseCase,
        insertConsigneeUseCase: InsertConsigneeUseCase,
        getAllConsigneeUseCase: GetAllConsigneesUseCase,
        getConsigneeUseCase: GetConsigneeUseCase,
        updateConsigneeUseCase: UpdateConsigneeUseCase
    ): ConsigneeUsesCases = ConsigneeUsesCases(
        deleteConsigneeUseCase,
        insertConsigneeUseCase,
        getAllConsigneeUseCase,
        getConsigneeUseCase,
        updateConsigneeUseCase
    )

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

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideSharedPrefUserStorageUsesCases(
        deleteUIDUseCase: DeleteUIDUseCase,
        getUIDUseCase: GetUIDUseCase,
        saveUIDUsesCase: SaveUIDUsesCase
    ): SharedPrefUserStorageUsesCases =
        SharedPrefUserStorageUsesCases(deleteUIDUseCase, getUIDUseCase, saveUIDUsesCase)

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        sharedPrefUserStorageUsesCases: SharedPrefUserStorageUsesCases
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        sharedPrefUserStorageUsesCases = sharedPrefUserStorageUsesCases
    )

    @Provides
    @Singleton
    fun providesSharedPrefUserStorageRepository(
        sharedPrefUserStorage: SharedPrefUserStorage
    ): SharedPrefUserStorageRepository =
        SharedPrefUserStorageRepositoryImpl(sharedPrefUserStorage = sharedPrefUserStorage)

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance("https://fir-practic-80202-default-rtdb.europe-west1.firebasedatabase.app/")

    @Provides
    @Singleton
    fun providesFirebaseRepository(
        firebaseDatabase: FirebaseDatabase,
        getUIDUseCase: GetUIDUseCase
    ): FirebaseService = FirebaseServiceImpl(firebaseDatabase, getUIDUseCase)
}