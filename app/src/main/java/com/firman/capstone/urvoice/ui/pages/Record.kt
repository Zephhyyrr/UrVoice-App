package com.firman.capstone.urvoice.ui.pages

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.*
import com.firman.capstone.urvoice.R
import com.firman.capstone.urvoice.ui.theme.*
import com.firman.capstone.urvoice.ui.viewmodel.SpeechViewModel
import com.firman.capstone.urvoice.utils.ResultState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeechViewModel = hiltViewModel(),
    onNavigateToSpeechToText: () -> Unit = {}
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    val speechToTextState by viewModel.speechToTextState.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val convertedText by viewModel.convertedText.collectAsStateWithLifecycle()

    var isConfirmationAccepted by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var recordedText by remember { mutableStateOf("") }

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    var showPermissionRationale by remember { mutableStateOf(false) }


    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (!isGranted) {
            showPermissionRationale = true
        }
    }

    val stopAnimationComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.stop_animation)
    )

    val micAnimationProgress by animateLottieCompositionAsState(
        composition = stopAnimationComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isRecording,
        speed = 1.0f,
        restartOnPlay = true
    )

    LaunchedEffect(speechToTextState) {
        if (speechToTextState is ResultState.Success && convertedText.isNotEmpty() && !isConfirmationAccepted) {
            recordedText = convertedText
            showConfirmationDialog = true
        }
    }

    LaunchedEffect(isConfirmationAccepted, speechToTextState) {
        if (isConfirmationAccepted && speechToTextState is ResultState.Success) {
            onNavigateToSpeechToText()
            isConfirmationAccepted = false
        }
    }

    val handleRecordingClick = remember {
        {
            if (!hasAudioPermission) {
                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                return@remember
            }

            if (speechToTextState is ResultState.Loading) {
                return@remember
            }
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

            if (isRecording) {
                viewModel.stopRecordingAndTranscribe()
            } else {
                viewModel.startRecording()
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text(
                    text = "Perekaman Berhasil!",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = textColor,
                        fontFamily = PoppinsMedium,
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "Apakah Anda ingin mengubah ini ke teks?",
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = textColor,
                            fontFamily = PoppinsMedium,
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        isConfirmationAccepted = true
                        onNavigateToSpeechToText()
                    }
                ) {
                    Text(
                        "Iya", style = TextStyle(
                            fontSize = 14.sp,
                            color = whiteColor,
                            fontFamily = PoppinsMedium,
                        )
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showConfirmationDialog = false
                        viewModel.resetState()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = primaryColor,
                    ),
                    border = BorderStroke(1.dp, primaryColor)
                ) {
                    Text(
                        "Rekam Ulang", style = TextStyle(
                            fontSize = 14.sp,
                            color = primaryColor,
                            fontFamily = PoppinsMedium,
                        )
                    )
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.record_title),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = whiteColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = when {
                        !hasAudioPermission -> "Izin mikrofon diperlukan untuk merekam. Ketuk mikrofon untuk mengizinkan."
                        isRecording -> "🔴 Sedang merekam... (Ketuk untuk berhenti)"
                        speechToTextState is ResultState.Loading -> "⏳ Memproses audio menjadi teks..."
                        speechToTextState is ResultState.Error -> "Terjadi kesalahan"
                        else -> "Tekan tombol mikrofon untuk memulai merekam"
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = textColor
                    ),
                    fontFamily = PoppinsMedium,
                    color = when {
                        !hasAudioPermission -> MaterialTheme.colorScheme.error
                        isRecording -> Color.Red
                        speechToTextState is ResultState.Loading -> MaterialTheme.colorScheme.primary
                        speechToTextState is ResultState.Error -> MaterialTheme.colorScheme.error
                        else -> textColor
                    },
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.weight(1f))

                val loadingAnimationComposition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.loading_animation)
                )
                val loadingAnimationProgress by animateLottieCompositionAsState(
                    composition = loadingAnimationComposition,
                    iterations = LottieConstants.IterateForever,
                    isPlaying = true
                )

                if (speechToTextState is ResultState.Loading && !isRecording) {
                    Box(
                        modifier = Modifier
                            .size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimation(
                            composition = loadingAnimationComposition,
                            progress = { loadingAnimationProgress },
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                if (convertedText.isNotEmpty() && !showConfirmationDialog) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.resetState()
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset", fontFamily = PoppinsMedium)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Simpan", fontFamily = PoppinsMedium)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isRecording && stopAnimationComposition != null -> {
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .clip(CircleShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = LocalIndication.current,
                                    onClick = handleRecordingClick
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieAnimation(
                                composition = stopAnimationComposition,
                                progress = { micAnimationProgress },
                                modifier = Modifier.size(350.dp)
                            )
                        }
                    }

                    speechToTextState is ResultState.Loading -> {
                        val loadingAnimationComposition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.loading_animation)
                        )
                        val loadingAnimationProgress by animateLottieCompositionAsState(
                            composition = loadingAnimationComposition,
                            iterations = LottieConstants.IterateForever,
                            isPlaying = true
                        )

                        LottieAnimation(
                            composition = loadingAnimationComposition,
                            progress = { loadingAnimationProgress },
                            modifier = Modifier.size(150.dp)
                        )
                    }

                    else -> {
                        val micAnimationComposition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.mic_animation)
                        )
                        val micAnimationProgress by animateLottieCompositionAsState(
                            composition = micAnimationComposition,
                            iterations = LottieConstants.IterateForever,
                            isPlaying = true
                        )

                        Box(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = LocalIndication.current,
                                    onClick = handleRecordingClick
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieAnimation(
                                composition = micAnimationComposition,
                                progress = { micAnimationProgress },
                                modifier = Modifier.size(350.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}