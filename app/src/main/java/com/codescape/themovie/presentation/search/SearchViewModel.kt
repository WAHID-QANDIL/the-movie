package com.codescape.themovie.presentation.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codescape.themovie.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@FlowPreview
@HiltViewModel
class SearchViewModel @Inject constructor(
    searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {
    val uiState = MutableStateFlow(SearchUiState())

    private val _search = mutableStateOf(TextFieldValue())

    val search = snapshotFlow { _search.value }

    private val movies =
        search
            .debounce(0.2.seconds)
            .flatMapLatest { search ->
                searchMoviesUseCase(search.text)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds),
                initialValue = emptyList()
            )

    init {
        combine(search, movies) { search, movies ->
            uiState.update {
                it.copy(
                    search = search,
                    movies = movies
                )
            }
        }.launchIn(viewModelScope)
    }

    fun search(value: TextFieldValue) {
        _search.value = value
    }

    fun clear() {
        _search.value = TextFieldValue()
    }
}
