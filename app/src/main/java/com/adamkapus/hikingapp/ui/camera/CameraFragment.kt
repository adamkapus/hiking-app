package com.adamkapus.hikingapp.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface.ROTATION_0
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentCameraScreenBinding
import com.adamkapus.hikingapp.ml.ConvModMeta
import com.adamkapus.hikingapp.ui.camera.CameraUIState.*
import com.adamkapus.hikingapp.ui.camera.adapter.RecognitionAdapter
import com.adamkapus.hikingapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class CameraFragment : Fragment() {

    @Inject
    lateinit var flowerResolver: FlowerResolver

    private lateinit var binding: FragmentCameraScreenBinding
    private lateinit var adapter: RecognitionAdapter
    private lateinit var submitButton: Button
    private lateinit var startButton: Button

    companion object {
        private val REQUIRED_PERMISSIONS_FOR_ANALYSIS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        private val REQUIRED_PERMISSIONS_FOR_SUBMISSION =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
    }

    private lateinit var preview: Preview
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var imageCapture: ImageCapture


    private var capturedImage: Bitmap? = null


    private lateinit var camera: Camera
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val viewModel: CameraViewModel by viewModels()

    private val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                startSubmitting()
            } else if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                startSubmitting()
            } else {
                showSnackbar(R.string.permission_location_submitting_reasoning)
            }

        }

    private val analysisPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (requireContext().hasAllPermissions(REQUIRED_PERMISSIONS_FOR_ANALYSIS)) {
                onStartButtonPressed()
            } else {
                showSnackbar(R.string.permission_camera_reasoning)
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraScreenBinding.inflate(layoutInflater, container, false)
        submitButton = binding.submitButton
        startButton = binding.startCameraButton
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        startButton.setOnClickListener {
            onStartButtonPressed()
        }
        submitButton.setOnClickListener {
            onSubmitButtonPressed()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }

    }

    private fun render(uiState: CameraUIState) {
        when (uiState) {
            is ReadyForRecognition -> {
                submitButton.isEnabled = false
                startButton.isEnabled = true
            }
            is RecognitionInProgress -> {
                submitButton.isEnabled = false
                startButton.isEnabled = false
                updateList(uiState.recognitions)
            }
            is RecognitionFinished -> {
                submitButton.isEnabled = true
                startButton.isEnabled = true
                updateList(uiState.recognitions)
                takeImageThenStopRecognition()
            }
            is SubmissionInProgress -> {
                submitButton.isEnabled = false
                startButton.isEnabled = false
            }
            is SubmissionSuccessful -> {
                showSnackbar(R.string.camera_submission_success)
                viewModel.handledSubmissionSuccess()
            }
            is SubmissionFailed -> {
                showSnackbar(R.string.camera_submission_failed)
                viewModel.handledSubmissionFailed()
            }

        }
    }


    private fun setUpRecyclerView() {
        adapter = RecognitionAdapter(requireContext())
        binding.recognitionResults.adapter = adapter
        binding.recognitionResults.itemAnimator = null
    }

    private fun updateList(recognitions: List<Recognition>) {
        val displayableRecognitions = recognitions.map {
            Recognition(
                label = flowerResolver.getDisplayName(it.label),
                confidence = it.confidence
            )
        }
        adapter.submitList(displayableRecognitions)
    }

    private fun onStartButtonPressed() {
        if (requireContext().hasAllPermissions(REQUIRED_PERMISSIONS_FOR_ANALYSIS)) {
            startRecognition()
            viewModel.onRecognitionSessionStarted()
        } else {
            analysisPermissionRequest.launch(REQUIRED_PERMISSIONS_FOR_ANALYSIS)
        }
    }

    private fun onSubmitButtonPressed() {
        if (requireContext().hasLocationPermission()) {
            startSubmitting()
        } else {
            locationPermissionRequest.launch(REQUIRED_PERMISSIONS_FOR_SUBMISSION)
        }
    }

    private fun startSubmitting() {
        viewModel.submitRecognition(capturedImage)
    }

    private fun startRecognition() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder()
                .build()

            val rotation = view?.display?.rotation ?: ROTATION_0
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(rotation)
                .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()



            imageAnalyzer = ImageAnalysis.Builder()

                .setTargetResolution(Size(224, 224))

                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysisUseCase: ImageAnalysis ->
                    analysisUseCase.setAnalyzer(
                        cameraExecutor,
                        CameraFragment.FlowerImageAnalyzer(requireContext()) { items ->

                            viewModel.onRecognitionMade(items)
                        })
                }


            val cameraSelector =
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                    CameraSelector.DEFAULT_BACK_CAMERA
                } else {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                }

            try {

                cameraProvider.unbindAll()


                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer, imageCapture
                )


                preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch (_: Exception) {

            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takeImageThenStopRecognition() {
        imageCapture.takePicture(
            cameraExecutor,
            @ExperimentalGetImage object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val image = imageProxy.image
                    if (image == null) {
                        capturedImage = null
                        stopRecognition()
                    } else {
                        when (image.format) {
                            ImageFormat.YUV_420_888 -> {
                                val yuvToRgbConverter = YuvToRgbConverterUtil(requireContext())
                                val bitmap =
                                    Bitmap.createBitmap(
                                        image.width,
                                        image.height,
                                        Bitmap.Config.ARGB_8888
                                    )
                                yuvToRgbConverter.yuvToRgb(image, bitmap)
                                capturedImage = bitmap
                            }
                            ImageFormat.JPEG -> {
                                val byteArray = jpegImageToJpegByteArray(image)
                                val simpleBitmap =
                                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                val rotationMatrix: Matrix = Matrix()
                                rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                                val rotatedBitmap = Bitmap.createBitmap(
                                    simpleBitmap,
                                    0,
                                    0,
                                    simpleBitmap.width,
                                    simpleBitmap.height,
                                    rotationMatrix,
                                    false
                                )
                                capturedImage = rotatedBitmap
                            }
                            else -> {
                                capturedImage = null
                            }
                        }

                        stopRecognition()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    capturedImage = null
                    stopRecognition()
                }
            }
        )
    }

    private fun jpegImageToJpegByteArray(image: Image): ByteArray {
        val planes = image.planes
        val buffer = planes[0].buffer
        val array = ByteArray(buffer.capacity())
        buffer.get(array)
        return array
    }

    private fun stopRecognition() {
        requireActivity().runOnUiThread {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }
    }

    //ImageAnalysis.analizer implementálása, amiben a kép alapján a TF Lite modellt futtatjuk
    private class FlowerImageAnalyzer(
        ctx: Context,
        private val listener: (recognition: List<Recognition>) -> Unit
    ) :
        ImageAnalysis.Analyzer {


        private val flowerModel: ConvModMeta by lazy {

            val compatList = CompatibilityList()

            val options = if (compatList.isDelegateSupportedOnThisDevice) {
                Model.Options.Builder().setDevice(Model.Device.GPU).build()
            } else {
                Model.Options.Builder().setNumThreads(4).build()
            }

            ConvModMeta.newInstance(ctx, options)
        }

        override fun analyze(imageProxy: ImageProxy) {

            val items = mutableListOf<Recognition>()

            val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))

            val outputs = flowerModel.process(tfImage)
                .probabilityAsCategoryList.apply {
                    sortByDescending { it.score }
                }

            for (output in outputs) {
                items.add(Recognition(output.label, output.score))
            }



            listener(items.toList())


            imageProxy.close()
        }

        /**
         * Convert Image Proxy to Bitmap
         */
        private val yuvToRgbConverter = YuvToRgbConverterUtil(ctx)
        private lateinit var bitmapBuffer: Bitmap
        private lateinit var rotationMatrix: Matrix

        @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
        private fun toBitmap(imageProxy: ImageProxy): Bitmap? {

            val image = imageProxy.image ?: return null


            if (!::bitmapBuffer.isInitialized) {

                rotationMatrix = Matrix()
                rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                bitmapBuffer = Bitmap.createBitmap(
                    imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
                )
            }


            yuvToRgbConverter.yuvToRgb(image, bitmapBuffer)


            return Bitmap.createBitmap(
                bitmapBuffer,
                0,
                0,
                bitmapBuffer.width,
                bitmapBuffer.height,
                rotationMatrix,
                false
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}