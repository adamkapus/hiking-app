package com.adamkapus.hikingapp.ui.camera

sealed class CameraUIState {
    object ReadyForRecognition : CameraUIState()
    data class RecognitionInProgress(val recognitions: List<Recognition>) : CameraUIState()
    data class RecognitionFinished(val recognitions: List<Recognition>) : CameraUIState()
    object SubmissionInProgress : CameraUIState()
    object SubmissionSuccessful : CameraUIState()
    object SubmissionFailed : CameraUIState()
}

