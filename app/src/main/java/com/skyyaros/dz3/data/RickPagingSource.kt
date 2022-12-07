package com.skyyaros.dz3.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skyyaros.dz3.entity.RickPersonalWithLocation
import kotlinx.coroutines.flow.MutableStateFlow

class RickPagingSource(
    private val name: String?,
    private val species: String?,
    private val type: String?,
    private val status: String?,
    private val gender: String?
): PagingSource<Int, RickPersonalWithLocation>() {
    override fun getRefreshKey(state: PagingState<Int, RickPersonalWithLocation>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RickPersonalWithLocation> {
        val page = params.key ?: FIRST_PAGE
        val res = RickRepository.getPersonalities(page, name, species, type, status, gender)
        return if (res != null) {
            LoadResult.Page(data = res, prevKey = null, nextKey = if (res.isEmpty()) null else page + 1)
        } else {
            LoadResult.Error(Throwable("Error"))
        }
    }

    private companion object {
        private const val FIRST_PAGE = 1
    }
}