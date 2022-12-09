package com.adamkapus.hikingapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.route.RouteInteractor
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.ui.home.HomeUiState.Initial
import com.adamkapus.hikingapp.ui.home.HomeUiState.RoutesLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeInteractor: RouteInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(Initial)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadRoutes() = viewModelScope.launch {
        val resp = routeInteractor.getRoutes()
        when (resp) {
            is InteractorError -> {}
            is InteractorResult -> {
                _uiState.update { RoutesLoaded(resp.result) }
            }
        }
    }
}