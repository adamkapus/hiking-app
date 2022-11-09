package com.adamkapus.hikingapp.ui.camera

sealed class CameraScreenUIState{
    object ReadyForRecognition : CameraScreenUIState()
    data class RecognitionInProgress(val list: String) : CameraScreenUIState()
    data class RecognitionMade(val list: String) : CameraScreenUIState()
    data class SubmitRecognition(val flower: String) : CameraScreenUIState()
    object SubmissionInProgress : CameraScreenUIState()
    data class RecognitionSubmitted(val success: Boolean) : CameraScreenUIState()
}

