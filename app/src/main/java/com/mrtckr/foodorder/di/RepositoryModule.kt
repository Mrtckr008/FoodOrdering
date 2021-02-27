package com.mrtckr.foodorder.di

import com.mrtckr.foodorder.data.datasource.BasketRemoteDataSource
import com.mrtckr.foodorder.data.datasource.MenuRemoteDataSource
import com.mrtckr.foodorder.data.datasource.remote.BasketRemoteDataSourceImpl
import com.mrtckr.foodorder.data.datasource.remote.MenuRemoteDataSourceImpl
import com.mrtckr.foodorder.data.repository.BasketRepositoryImpl
import com.mrtckr.foodorder.data.repository.MenuRepositoryImpl
import com.mrtckr.foodorder.domain.repository.BasketRepository
import com.mrtckr.foodorder.domain.repository.MenuRepository
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