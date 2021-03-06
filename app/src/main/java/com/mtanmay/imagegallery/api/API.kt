package com.mtanmay.imagegallery.api

import com.mtanmay.imagegallery.BuildConfig
import org.jetbrains.annotations.NotNull
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    companion object {
        const val BASE_URL = "https://api.flickr.com/services/rest/"
    }

    @GET("?method=flickr.photos.getRecent")
    suspend fun getRecentPhotos(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 2,
        @Query("extras") extras: String = "url_s",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1"
    ): Response

    @GET("?method=flickr.photos.search")
    suspend fun getSearchPhotos(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("text") query: String,
        @Query("extras") extras: String = "url_s",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1"
    ): Response

}