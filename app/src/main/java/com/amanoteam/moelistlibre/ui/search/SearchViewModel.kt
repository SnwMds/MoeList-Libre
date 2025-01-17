package com.amanoteam.moelistlibre.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.amanoteam.moelistlibre.App
import com.amanoteam.moelistlibre.data.model.ApiParams
import com.amanoteam.moelistlibre.data.paging.AnimeListPaging
import com.amanoteam.moelistlibre.data.paging.MangaListPaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun setQuery(value: String) {
        _query.value = value
        params.value.q = _query.value
    }

    private val nsfw = MutableStateFlow(0)
    fun setNsfw(value: Int) {
        nsfw.value = value
        params.value.nsfw = nsfw.value
    }

    private val params = MutableStateFlow(
        ApiParams(
            q = query.value,
            nsfw = nsfw.value,
            fields = FIELDS
        )
    )

    val animeListFlow = Pager(
        PagingConfig(pageSize = 15, prefetchDistance = 10)
    ) {
        AnimeListPaging(App.api, params.value)
    }.flow
        .cachedIn(viewModelScope)

    val mangaListFlow = Pager(
        PagingConfig(pageSize = 15, prefetchDistance = 10)
    ) {
        MangaListPaging(App.api, params.value)
    }.flow
        .cachedIn(viewModelScope)

    companion object {
        private const val FIELDS = "id,title,main_picture,mean,media_type,num_episodes,num_chapters,start_season"
    }
}