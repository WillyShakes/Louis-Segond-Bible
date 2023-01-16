package org.redesnac.lsgbible.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.redesnac.lsgbible.common.Constants
import org.redesnac.lsgbible.data.remote.BibleApi1
import org.redesnac.lsgbible.data.remote.BibleApi2
import org.redesnac.lsgbible.data.repository.BookRepositoryImpl
import org.redesnac.lsgbible.domain.repository.BookRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBibleApi1(): BibleApi1 {
        return getRetrofitBuilder(Constants.BASE_URL1)
            .build()
            .create(BibleApi1::class.java)
    }

    @Provides
    @Singleton
    fun provideBibleApi2(): BibleApi2 {
        return getRetrofitBuilder(Constants.BASE_URL2)
            .build()
            .create(BibleApi2::class.java)
    }

    fun getRetrofitBuilder(baseUrl: String): Retrofit.Builder
    {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(client)

    }


    @Provides
    @Singleton
    fun provideBookRepository(api1: BibleApi1, api2: BibleApi2): BookRepository {
        return BookRepositoryImpl(api1, api2)
    }
}