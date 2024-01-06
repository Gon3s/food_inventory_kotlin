package com.gones.foodinventorykotlin.presentation.pages.scan

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    onBarcodeDetected: (barcodes: List<Barcode>) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context)

            Preview.Builder().build().setSurfaceProvider(previewView.surfaceProvider)

            previewView.controller = controller

            controller.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                BarCodeAnalyser { barcodes ->
                    onBarcodeDetected(barcodes)
                }
            )

            controller.cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            controller.imageAnalysisBackpressureStrategy =
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST

            controller.bindToLifecycle(lifecycleOwner)

            previewView
        },
        modifier = modifier
    )
}