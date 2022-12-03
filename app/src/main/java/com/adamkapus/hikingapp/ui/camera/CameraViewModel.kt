package com.adamkapus.hikingapp.ui.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.analysis.AnalysisInteractor
import com.adamkapus.hikingapp.domain.interactor.camera.FlowerImageSubmissionInteractor
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.ui.camera.CameraScreenUIState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val flowerImageSubmissionInteractor: FlowerImageSubmissionInteractor,
    private val analysisInteractor: AnalysisInteractor
) : ViewModel() {
    companion object {
        private const val maxAnalysisRounds = 10 //hány kör predikciót használjunk
        private const val maxDisplayedRecognition = 5
    }


    private lateinit var currentRecognitionSession: RecognitionSession

    private val _uiState = MutableStateFlow<CameraScreenUIState>(ReadyForRecognition)
    val uiState: StateFlow<CameraScreenUIState> = _uiState.asStateFlow()

    //Todo permission denied event, successful submission event


    //ToDo error képernyő
    fun onRecognitionMade(newRecognitions: List<Recognition>) {
        if (_uiState.value is RecognitionFinished) {
            return
        }
        val resp = analysisInteractor.calculateRecognitionSession(
            currentRecognitionSession,
            newRecognitions
        )
        when (resp) {
            is InteractorError -> {}
            is InteractorResult -> {
                val newRecognitionSession = resp.result
                currentRecognitionSession = newRecognitionSession
                val newRecognitionList =
                    getRecognitionListFromRecognitionSession(newRecognitionSession)

                if (newRecognitionSession.numberOfAnalysisRoundsDone >= maxAnalysisRounds) {
                    _uiState.update { RecognitionFinished(newRecognitionList) }
                } else {
                    _uiState.update { RecognitionInProgress(newRecognitionList) }
                }
            }
        }
    }

    fun onRecognitionSessionStarted() {
        currentRecognitionSession = RecognitionSession(
            currentRecognitionList = listOf(),
            cumulatedRecognitions = listOf(),
            numberOfAnalysisRoundsDone = 0,
            maxAnalysisRounds = maxAnalysisRounds
        )
        _uiState.update { RecognitionInProgress(listOf()) }
    }

    private fun getRecognitionListFromRecognitionSession(recognitionSession: RecognitionSession): List<Recognition> {
        val startingList = recognitionSession.currentRecognitionList.toMutableList()
        val orderedAndTrimmedList = startingList.sortedByDescending { it.confidence }.take(
            maxDisplayedRecognition
        )
        return orderedAndTrimmedList
    }


    private fun finishRecognitionSession() {

    }

    fun submitRecognition(image: Bitmap?) = viewModelScope.launch {
        Log.d("PLS", "Viewmodelben")
        _uiState.update { SubmissionInProgress }
        val resp = flowerImageSubmissionInteractor.submitFlowerImageWithLocation(
            flowerName = currentRecognitionSession.currentRecognitionList.sortedByDescending { it.confidence }[0].label,
            image = image
        )
        Log.d("PLS", resp.toString())
        when (resp) {
            is InteractorError -> {
                _uiState.update { SubmissionFailed }
            }
            is InteractorResult -> {
                _uiState.update { SubmissionSuccessful }
            }
        }
    }

    fun onPermissionsDenied() {

    }

}

//Egy felismerés - címke, valószínűség és százalékos forma
data class Recognition(var label: String, var confidence: Float) {

    override fun toString(): String {
        return "$label / $probabilityString"
    }

    val probabilityString = String.format("%.1f%%", confidence * 100.0f)

}

data class RecognitionSession(
    var currentRecognitionList: List<Recognition>,

    var cumulatedRecognitions: List<Recognition>,
    var numberOfAnalysisRoundsDone: Int,
    val maxAnalysisRounds: Int
)