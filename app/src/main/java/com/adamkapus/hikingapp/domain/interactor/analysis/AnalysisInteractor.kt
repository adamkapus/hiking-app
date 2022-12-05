package com.adamkapus.hikingapp.domain.interactor.analysis

import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.ui.camera.Recognition
import com.adamkapus.hikingapp.ui.camera.RecognitionSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnalysisInteractor @Inject constructor() {
    suspend fun calculateRecognitionSession(
        previousRecognitionSession: RecognitionSession,
        newRecognitions: List<Recognition>
    ): InteractorResponse<RecognitionSession> = withContext(Dispatchers.IO) {
        val newNumberOfAnalysisRoundsDone =
            previousRecognitionSession.numberOfAnalysisRoundsDone + 1
        val cumulatedRecognitions = previousRecognitionSession.cumulatedRecognitions.toMutableList()

        newRecognitions.forEach { newRec ->
            val el = cumulatedRecognitions.find { it.label == newRec.label };
            if (el == null) {
                cumulatedRecognitions.add(newRec)
            } else {
                val idx = cumulatedRecognitions.indexOf(el); cumulatedRecognitions[idx] =
                    Recognition(label = el.label, confidence = el.confidence + newRec.confidence)
            }
        }

        val currentRecognitionList = (cumulatedRecognitions.map { Rec ->
            Recognition(
                Rec.label,
                Rec.confidence / newNumberOfAnalysisRoundsDone
            )
        }.toMutableList())

        InteractorResult(
            RecognitionSession(
                currentRecognitionList = currentRecognitionList,
                cumulatedRecognitions = cumulatedRecognitions,
                numberOfAnalysisRoundsDone = newNumberOfAnalysisRoundsDone,
                maxAnalysisRounds = previousRecognitionSession.maxAnalysisRounds
            )
        )
    }

}