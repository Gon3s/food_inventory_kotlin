package com.gones.foodinventorykotlin.presentation.pages.scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.TorchState
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.presentation.components.scaffold.ScaffoldState
import com.gones.foodinventorykotlin.presentation.navigation.HomeRoute
import com.gones.foodinventorykotlin.presentation.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalMaterial3Api
@Composable
fun ScanScreen(
    scaffoldState: ScaffoldState,
    navController: NavController,
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_ANALYSIS
            )
        }
    }

    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    val screen = scaffoldState.currentScreen as? Screen.Scan
    LaunchedEffect(key1 = screen) {
        screen?.actions?.onEach { action ->
            when (action) {
                Screen.Scan.Actions.NavigationIcon -> {
                    navController.popBackStack()
                }
            }
        }?.launchIn(this)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (hasCamPermission) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CameraPreview(
                    controller = controller,
                    onBarcodeDetected = { barcodes ->
                        navController.navigate("product?barcode=${barcodes.firstOrNull()?.rawValue}") {
                            popUpTo(HomeRoute)
                        }
                    }
                )

                FlashIcon(
                    controller = controller,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .height(300.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ) {
                    Button(
                        onClick = { navController.navigate("product") { popUpTo(HomeRoute) } }
                    ) {
                        Text(text = stringResource(id = R.string.add_without_barcode))
                    }
                }
            }
        }
    }
}

@Composable
fun FlashIcon(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val flashOn = remember { mutableStateOf(false) }
    val icon = if (flashOn.value) {
        R.drawable.ic_flash_off
    } else {
        R.drawable.ic_flash_on
    }

    FilledIconButton(
        onClick = {
            flashOn.value = !flashOn.value
            // controller.enableTorch(flashOn.value)
            controller.imageCaptureFlashMode = if (flashOn.value) {
                TorchState.ON
            } else {
                TorchState.OFF
            }
        },
        modifier = modifier,
        content = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    )
}