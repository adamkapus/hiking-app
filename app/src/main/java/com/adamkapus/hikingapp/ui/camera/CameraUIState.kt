package com.adamkapus.hikingapp.ui.camera

sealed class CameraUIState {
    object ReadyForRecognition : CameraUIState()

    //object RecognitionStarted : CameraScreenUIState() //~event
    data class RecognitionInProgress(val recognitions: List<Recognition>) : CameraUIState()
    data class RecognitionFinished(val recognitions: List<Recognition>) : CameraUIState()
    object SubmissionInProgress : CameraUIState() //~event
    object SubmissionSuccessful : CameraUIState()
    object SubmissionFailed : CameraUIState()
    /*data class SubmitRecognition(val flower: String) : CameraScreenUIState()
    object SubmissionInProgress : CameraScreenUIState()
    data class RecognitionSubmitted(val success: Boolean) : CameraScreenUIState()*/
}

