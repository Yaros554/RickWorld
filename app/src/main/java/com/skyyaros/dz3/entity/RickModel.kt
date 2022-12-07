package com.skyyaros.dz3.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RickResponse(
    @Json(name = "results") val result: List<RickPersonal>
)

@JsonClass(generateAdapter = true)
data class RickPersonal(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "species") val species: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "location") val location: Location,
    @Json(name = "image") val image: String,
    @Json(name = "episode") val episodes: List<String>
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)

@JsonClass(generateAdapter = true)
data class DetailLocation(
    @Json(name = "type") val type: String,
    @Json(name = "dimension") val dimension: String
)

@JsonClass(generateAdapter = true)
data class Episode(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "air_date") val date: String,
    @Json(name = "episode") val unicCode: String
)

data class RickPersonalWithLocation(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val location: Location,
    val image: String,
    val type: String,
    val dimension: String,
    val episodes: List<Episode>
)