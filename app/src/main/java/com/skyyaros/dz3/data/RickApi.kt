package com.skyyaros.dz3.data

import com.skyyaros.dz3.entity.DetailLocation
import com.skyyaros.dz3.entity.Episode
import com.skyyaros.dz3.entity.RickResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

private const val BASE_URL = "https://rickandmortyapi.com/api/"

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val rickPersonalityApi = retrofit.create(RickPersonalityApi::class.java)
}

interface RickPersonalityApi {
    @GET("character")
    suspend fun getPersonalities(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("status") status: String?,
        @Query("gender") gender: String?
    ): Response<RickResponse>
    @GET
    suspend fun getLocationDetail(@Url url:String): Response<DetailLocation>
    @GET
    suspend fun getEpisodes(@Url url:String): Response<List<Episode>>
}