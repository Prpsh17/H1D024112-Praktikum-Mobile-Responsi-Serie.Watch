package com.pemmob.responsi.data.network

import com.pemmob.responsi.data.model.TenraiAnimeDetailResponse
import com.pemmob.responsi.data.model.TenraiAnimeListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TenraiApiService {

    @GET("anime")
    suspend fun getAnimeList(
        @Query("q") query: String? = null,
        @Query("genres") genres: String? = null,
        @Query("genre") genreId: Int? = null
    ): TenraiAnimeListResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): TenraiAnimeDetailResponse
}

