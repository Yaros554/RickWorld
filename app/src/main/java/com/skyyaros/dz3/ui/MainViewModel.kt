package com.skyyaros.dz3.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.skyyaros.dz3.data.RickPagingSource
import com.skyyaros.dz3.entity.RickPersonalWithLocation

class MainViewModel: ViewModel() {
    var name: String? = null
    var species: String? = null
    var type: String? = null
    var status: String? = null
    var gender: String? = null

    val pageData by lazy {
        return@lazy Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { RickPagingSource(name, species, type, status, gender) }
        ).flow.cachedIn(viewModelScope)
    }
    var pageDataSnapShot = emptyList<RickPersonalWithLocation>()
    var lastIndex = 0
    var lastOffset = 0
}