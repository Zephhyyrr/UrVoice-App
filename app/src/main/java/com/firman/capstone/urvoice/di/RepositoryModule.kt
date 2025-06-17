package com.firman.capstone.urvoice.di

import com.firman.capstone.urvoice.data.repository.article.ArticleRepository
import com.firman.capstone.urvoice.data.repository.article.ArticleRepositoryImpl
import com.firman.capstone.urvoice.data.repository.login.LoginRepository
import com.firman.capstone.urvoice.data.repository.login.LoginRepositoryImpl
import com.firman.capstone.urvoice.data.repository.register.RegisterRepository
import com.firman.capstone.urvoice.data.repository.register.RegisterRepositoryImpl
import com.firman.capstone.urvoice.data.repository.user.UserRepository
import com.firman.capstone.urvoice.data.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(registerRepositoryImpl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}