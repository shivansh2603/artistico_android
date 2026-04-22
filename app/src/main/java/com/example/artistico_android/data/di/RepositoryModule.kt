package com.example.artistico_android.data.di

import com.example.artistico_android.core.common.DefaultDispatcherProvider
import com.example.artistico_android.core.common.DispatcherProvider
import com.example.artistico_android.data.fake.FakeAuthRepository
import com.example.artistico_android.data.fake.FakeChallengeRepository
import com.example.artistico_android.data.fake.FakeConnectRepository
import com.example.artistico_android.data.fake.FakePostRepository
import com.example.artistico_android.data.fake.FakeProfileRepository
import com.example.artistico_android.domain.repo.AuthRepository
import com.example.artistico_android.domain.repo.ChallengeRepository
import com.example.artistico_android.domain.repo.ConnectRepository
import com.example.artistico_android.domain.repo.PostRepository
import com.example.artistico_android.domain.repo.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindings {

    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: FakePostRepository): PostRepository

    @Binds
    @Singleton
    abstract fun bindConnectRepository(impl: FakeConnectRepository): ConnectRepository

    @Binds
    @Singleton
    abstract fun bindChallengeRepository(impl: FakeChallengeRepository): ChallengeRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: FakeProfileRepository): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider = DefaultDispatcherProvider()
}
