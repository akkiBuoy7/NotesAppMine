package com.cn.notesappkotlin.di

import com.cn.notesappkotlin.api.AuthInterceptor
import com.cn.notesappkotlin.api.NoteApi
import com.cn.notesappkotlin.api.UserApi
import com.cn.notesappkotlin.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {


    @Singleton
    @Provides
    fun providesRetrofitBuilder():Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun providesOKHTTPClient(authInterceptor: AuthInterceptor):OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder):UserApi{
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesNoteAPI(okHttpClient: OkHttpClient,retrofitBuilder: Retrofit.Builder):NoteApi{
        return retrofitBuilder.client(okHttpClient).build().create(NoteApi::class.java)
    }
}