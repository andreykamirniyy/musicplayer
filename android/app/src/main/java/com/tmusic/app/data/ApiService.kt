package com.tmusic.app.data

import com.tmusic.app.model.Track
import com.tmusic.app.model.WaveResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class FavoritePayload(val track_id: Int)

interface ApiService {
    @GET("tracks")
    suspend fun getTracks(): List<Track>

    @POST("favorites/{userId}")
    suspend fun addFavorite(
        @Path("userId") userId: String,
        @Body payload: FavoritePayload,
    )

    @GET("wave/{userId}")
    suspend fun getWave(
        @Path("userId") userId: String,
        @Query("mood") mood: String,
    ): WaveResponse
}
