package com.sonnt.fpmerchant.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sonnt.fpmerchant.data.local.AuthDataSource
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.network.service.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Endpoint.BASE_URL)
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor {chain ->
            val token = AuthDataSource.authToken
            val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${token}").build()
            chain.proceed(request)
        }.build())
        .addConverterFactory(GsonConverterFactory.create(AppModule.provideGson()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    var authService: AuthService = retrofit.create(AuthService::class.java)
    var orderService: OrderService = retrofit.create(OrderService::class.java)
    var menuService: ProductMenuService = retrofit.create(ProductMenuService::class.java)
    var productService: ProductService = retrofit.create(ProductService::class.java)
    var infoService: InfoService = retrofit.create(InfoService::class.java)
}