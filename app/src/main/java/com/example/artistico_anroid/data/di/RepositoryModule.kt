package com.example.artistico_anroid.data.di

import com.example.artistico_anroid.core.common.DefaultDispatcherProvider
import com.example.artistico_anroid.core.common.DispatcherProvider
import com.example.artistico_anroid.data.fake.FakeChallengeRepository
import com.example.artistico_anroid.data.fake.FakeConnectRepository
import com.example.artistico_anroid.data.fake.FakePostRepository
import com.example.artistico_anroid.data.fake.FakeProfileRepository
import com.example.artistico_anroid.domain.repo.ChallengeRepository
import com.example.artistico_anroid.domain.repo.ConnectRepository
import com.example.artistico_anroid.domain.repo.PostRepository
import com.example.artistico_anroid.domain.repo.ProfileRepository
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
}

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider = DefaultDispatcherProvider()
}
