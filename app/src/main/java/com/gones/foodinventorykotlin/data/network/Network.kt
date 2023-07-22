package com.gones.foodinventorykotlin.data.network

import com.gones.foodinventorykotlin.BuildConfig
import com.squareup.moshi.Moshi
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val WS_CALL_TIMEOUT_SECONDS = 60L

fun createApiClient(): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(wsHttpClient())
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()

fun wsHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(WSApiInterceptor())
        .callTimeout(WS_CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
        .build()

fun initSupabaseClient(): SupabaseClient = createSupabaseClient(
    BuildConfig.SUPABASE_URL,
    BuildConfig.SUPABASE_KEY
) {
    install(Postgrest)
}
