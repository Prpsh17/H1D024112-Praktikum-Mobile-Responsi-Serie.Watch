package com.pemmob.responsi.data.repository

import com.pemmob.responsi.data.model.TenraiAnimeDto
import com.pemmob.responsi.data.network.ApiClient
import com.pemmob.responsi.data.network.TenraiApiService

class AnimeRepository(
    private val apiService: TenraiApiService = ApiClient.service
) {

    suspend fun searchAnime(query: String?, genreId: Int?): List<TenraiAnimeDto> {
        val sanitizedQuery = query?.trim()?.ifBlank { null }
        val genreParam = genreId?.toString()

        val response = apiService.getAnimeList(
            query = sanitizedQuery,
            genres = genreParam,
            genreId = genreId
        )
        return response.data ?: emptyList()
    }

    suspend fun getAnimeDetail(id: Int): TenraiAnimeDto {
        val response = apiService.getAnimeDetail(id)
        return response.data ?: throw NoSuchElementException("Data anime tidak ditemukan")
    }
}