package com.example.yemeksiparisuygulamasi.di

import com.example.yemeksiparisuygulamasi.data.datasource.BasketRemoteDataSource
import com.example.yemeksiparisuygulamasi.data.datasource.MenuRemoteDataSource
import com.example.yemeksiparisuygulamasi.data.datasource.remote.BasketRemoteDataSourceImpl
import com.example.yemeksiparisuygulamasi.data.datasource.remote.MenuRemoteDataSourceImpl
import com.example.yemeksiparisuygulamasi.data.repository.BasketRepositoryImpl
import com.example.yemeksiparisuygulamasi.data.repository.MenuRepositoryImpl
import com.example.yemeksiparisuygulamasi.domain.repository.BasketRepository
import com.example.yemeksiparisuygulamasi.domain.repository.MenuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun providesMenuRepository(
        remoteDataSource: MenuRemoteDataSource
    ): MenuRepository {
        return MenuRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    fun providesMenuRemoteDataSource(): MenuRemoteDataSource {
        return MenuRemoteDataSourceImpl()
    }

    @Provides
    fun providesBasketRepository(
        remoteDataSource: BasketRemoteDataSource
    ): BasketRepository {
        return BasketRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    fun providesBasketRemoteDataSource(): BasketRemoteDataSource {
        return BasketRemoteDataSourceImpl()
    }
}