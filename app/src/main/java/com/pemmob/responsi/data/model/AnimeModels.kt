package com.pemmob.responsi.data.model

import com.google.gson.annotations.SerializedName

data class TenraiAnimeListResponse(
    @SerializedName("data") val data: List<TenraiAnimeDto>? = null
)

data class TenraiAnimeDetailResponse(
    @SerializedName("data") val data: TenraiAnimeDto? = null
)

data class TenraiAnimeDto(
    @SerializedName("mal_id") val malId: Int = 0,
    @SerializedName("id") val fallbackId: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("title_english") val titleEnglish: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("score") val score: Double? = null,
    @SerializedName("episodes") val episodes: Int? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("synopsis") val synopsis: String? = null,
    @SerializedName("poster") val poster: String? = null,
    @SerializedName("banner") val banner: String? = null,
    @SerializedName("images") val images: TenraiAnimeImagesDto? = null,
    @SerializedName("genres") val genres: List<TenraiGenreDto>? = null
) {
    val id: Int
        get() = if (malId != 0) malId else (fallbackId ?: 0)

    val displayTitle: String
        get() = titleEnglish?.takeIf { it.isNotBlank() } ?: title ?: "Tanpa Judul"

    val imageUrl: String?
        get() = poster
            ?: images?.webp?.largeImageUrl
            ?: images?.webp?.imageUrl
            ?: images?.jpg?.largeImageUrl
            ?: images?.jpg?.imageUrl

    val genreListText: String
        get() = if (!genres.isNullOrEmpty()) {
            genres.joinToString { it.name }
        } else {
            "Tidak ada genre"
        }
}

data class TenraiAnimeImagesDto(
    @SerializedName("jpg") val jpg: TenraiImageDetailDto? = null,
    @SerializedName("webp") val webp: TenraiImageDetailDto? = null
)

data class TenraiImageDetailDto(
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("small_image_url") val smallImageUrl: String? = null,
    @SerializedName("large_image_url") val largeImageUrl: String? = null
)

data class TenraiGenreDto(
    @SerializedName("mal_id") val malId: Int? = null,
    @SerializedName("id") val fallbackId: Int? = null,
    @SerializedName("name") val name: String
) {
    val genreId: Int
        get() = malId ?: fallbackId ?: 0
}

// Type aliases for seamless compatibility
typealias AnimeDto = TenraiAnimeDto
typealias GenreDto = TenraiGenreDto
typealias JikanAnimeListResponse = TenraiAnimeListResponse
typealias JikanAnimeDetailResponse = TenraiAnimeDetailResponse