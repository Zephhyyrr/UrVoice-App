package com.firman.capstone.urvoice.ui.pages

import android.Manifest
import android.media.MediaPlayer
import android.util.Log
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
import com.firman.capstone.urvoice.utils.MediaUrlUtils
import com.firman.capstone.urvoice.utils.ResultState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeechViewModel = hiltViewModel(),
    onNavigateToSpeechToText: (String) -> Unit = {} // Changed to accept audioFileName parameter
) {
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    val speechToTextState by viewModel.speechToTextState.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val convertedText by viewModel.convertedText.collectAsStateWithLifecycle()

    var audioUrl by remember { mutableStateOf<String?>(null) }
    var audioFileName by remember { mutableStateOf<String?>(null) }
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

    // Lottie animations
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

    // Handle speech-to-text result and setup audio
    LaunchedEffect(speechToTextState) {
        if (speechToTextState is ResultState.Success && convertedText.isNotEmpty() && !isConfirmationAccepted) {
            val data = (speechToTextState as ResultState.Success).data
            recordedText = data.text ?: ""
            audioUrl = MediaUrlUtils.buildMediaUrl(data.audioPath)
            audioFileName = data.audioFileName // This is from the API response

            // DEBUG: Add logging to verify the data
            Log.d("RecordScreen", "API Response Data:")
            Log.d("RecordScreen", "  - text: ${data.text}")
            Log.d("RecordScreen", "  - audioPath: ${data.audioPath}")
            Log.d("RecordScreen", "  - audioFileName: ${data.audioFileName}")
            Log.d("RecordScreen", "  - audioUrl: $audioUrl")

            showConfirmationDialog = true
        }
    }

    // Recording click handler
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

    // Confirmation dialog
    if (showConfirmationDialog) {
        AudioConfirmationDialog(
            audioUrl = audioUrl,
            onConfirm = {
                showConfirmationDialog = false
                isConfirmationAccepted = true
                // Pass the audioFileName from API response to navigation
                audioFileName?.let { fileName ->
                    Log.d("RecordScreen", "Navigating with audioFileName: $fileName")
                    onNavigateToSpeechToText(fileName)
                } ?: run {
                    Log.e("RecordScreen", "audioFileName is null, cannot navigate")
                }
            },
            onDismiss = {
                showConfirmationDialog = false
                viewModel.resetState()
            }
        )
    }

    // Rest of your Scaffold and UI code remains the same...
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

                // Status text
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
                        color = when {
                            !hasAudioPermission -> MaterialTheme.colorScheme.error
                            isRecording -> Color.Red
                            speechToTextState is ResultState.Loading -> MaterialTheme.colorScheme.primary
                            speechToTextState is ResultState.Error -> MaterialTheme.colorScheme.error
                            else -> textColor
                        },
                        fontFamily = PoppinsMedium
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))
                Spacer(modifier = Modifier.weight(1f))

                // Action buttons (only show when text is available and dialog is not shown)
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
                                // Add save functionality here
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Simpan", fontFamily = PoppinsMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Center microphone/animation area
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isRecording && stopAnimationComposition != null -> {
                        // Recording animation
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
                        // Loading animation
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
                        // Default microphone animation
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

@Composable
private fun AudioConfirmationDialog(
    audioUrl: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer() }

    // Setup MediaPlayer
    LaunchedEffect(audioUrl) {
        if (!audioUrl.isNullOrBlank()) {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(audioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    // Ready to play
                }
                mediaPlayer.setOnCompletionListener {
                    isPlaying = false
                }
            } catch (_: Exception) {
                isPlaying = false
            }
        }
    }

    // Release MediaPlayer saat keluar
    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Perekaman Berhasil!",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = textColor,
                    fontFamily = PoppinsMedium
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
                        fontFamily = PoppinsMedium
                    )
                )

                if (!audioUrl.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (isPlaying) {
                                mediaPlayer.pause()
                                isPlaying = false
                            } else {
                                mediaPlayer.start()
                                isPlaying = true
                            }
                        }
                    ) {
                        Text(if (isPlaying) "⏸ Pause Audio" else "▶ Play Audio")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    "Iya",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = whiteColor,
                        fontFamily = PoppinsMedium
                    )
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor
                ),
                border = BorderStroke(1.dp, primaryColor)
            ) {
                Text(
                    "Rekam Ulang",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = primaryColor,
                        fontFamily = PoppinsMedium
                    )
                )
            }
        }
    )
}