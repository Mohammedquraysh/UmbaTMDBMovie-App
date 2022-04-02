package com.example.tmdbumbaapp.dependencyinjection

import com.example.tmdbumbaapp.BASE_URL
import com.example.tmdbumbaapp.network.MoviesModuleApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        //okhttp interceptor is used to log retrofit responses especially when debugging.
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS).build()
            )
            .build()
    }

    //get api instance from retrofit
    @Singleton
    @Provides
    fun provideAuthModuleApi(): MoviesModuleApiInterface {
        return provideRetrofitInstance().create(MoviesModuleApiInterface::class.java)
    }
}