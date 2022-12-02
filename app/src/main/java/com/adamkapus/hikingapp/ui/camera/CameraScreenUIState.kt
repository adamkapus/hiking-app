package com.adamkapus.hikingapp.ui.camera

sealed class CameraScreenUIState {
    object ReadyForRecognition : CameraScreenUIState()

    //object RecognitionStarted : CameraScreenUIState() //~event
    data class RecognitionInProgress(val recognitions: List<Recognition>) : CameraScreenUIState()
    data class RecognitionFinished(val recognitions: List<Recognition>) : CameraScreenUIState()
    object SubmissionInProgress : CameraScreenUIState() //~event
    object SubmissionSuccessful : CameraScreenUIState()
    object SubmissionFailed : CameraScreenUIState()
    /*data class SubmitRecognition(val flower: String) : CameraScreenUIState()
    object SubmissionInProgress : CameraScreenUIState()
    data class RecognitionSubmitted(val success: Boolean) : CameraScreenUIState()*/
}

