package com.skyyaros.dz3.data

import com.skyyaros.dz3.entity.DetailLocation
import com.skyyaros.dz3.entity.Episode
import com.skyyaros.dz3.entity.RickPersonalWithLocation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

object RickRepository {
    suspend fun getPersonalities(
        page: Int,
        name: String?,
        species: String?,
        type: String?,
        status: String?,
        gender: String?
    ): List<RickPersonalWithLocation>? = coroutineScope {
        return@coroutineScope try {
            val response = RetrofitInstance.rickPersonalityApi.getPersonalities(page, name, species, type, status, gender)
            if (response.isSuccessful) {
                withContext(Dispatchers.Default) {
                    val personalities = response.body()!!.result
                    val deferLoc: List<Deferred<DetailLocation>> = personalities.map {
                        async {
                            if (it.location.url.isNotBlank()) {
                                val locId = it.location.url.split("/").last()
                                val innerResponse = RetrofitInstance.rickPersonalityApi.getLocationDetail("location/$locId")
                                if (innerResponse.isSuccessful) {
                                    innerResponse.body()!!
                                } else {
                                    DetailLocation(" ", " ")
                                }
                            } else {
                                DetailLocation(" ", " ")
                            }
                        }
                    }
                    val locations = deferLoc.awaitAll()

                    val deferEpisode: List<Deferred<List<Episode>>> = personalities.map {
                        async {
                            if (it.episodes.isNotEmpty()) {
                                val request = StringBuilder()
                                request.append("episode/[")
                                it.episodes.forEach {
                                    val temp = it.split("/").last() + ","
                                    request.append(temp)
                                }
                                request.setCharAt(request.length - 1, ']')
                                val innerResponse = RetrofitInstance.rickPersonalityApi.getEpisodes(request.toString())
                                if (innerResponse.isSuccessful) {
                                    innerResponse.body()!!
                                } else {
                                    emptyList()
                                }
                            } else {
                                emptyList()
                            }
                        }
                    }
                    val episodes = deferEpisode.awaitAll()

                    var index = -1
                    personalities.zip(locations).map {
                        val person = it.first
                        val loc = it.second
                        index++
                        RickPersonalWithLocation(person.id, person.name, person.status, person.species, person.gender, person.location, person.image, loc.type, loc.dimension, episodes[index])
                    }
                }
            } else {
                if (response.code() == 404) {
                    emptyList()
                } else {
                    null
                }
            }
        } catch (t: Throwable) {
            null
        }
    }
}