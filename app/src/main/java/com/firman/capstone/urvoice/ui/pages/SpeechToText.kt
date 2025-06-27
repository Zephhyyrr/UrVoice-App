package com.firman.capstone.urvoice.ui.pages

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firman.capstone.urvoice.R
import com.firman.capstone.urvoice.ui.components.SpeechToTextCard
import com.firman.capstone.urvoice.ui.theme.PoppinsSemiBold
import com.firman.capstone.urvoice.ui.theme.UrVoiceTheme
import com.firman.capstone.urvoice.ui.theme.primaryColor
import com.firman.capstone.urvoice.ui.theme.whiteBackground
import com.firman.capstone.urvoice.ui.theme.whiteColor
import com.firman.capstone.urvoice.ui.viewmodel.SpeechViewModel
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechToTextScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeechViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateRecordScreen: () -> Unit = {},
    onNavigateAnalyzeScreen: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var retryRecordStateButton by remember{mutableStateOf(SSButtonState.IDLE)}
    var analyzeStateButton by remember{mutableStateOf(SSButtonState.IDLE)}
    val convertedText = viewModel.convertedText.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.speech_to_text_title),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_white),
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SpeechToTextCard(
                        text = convertedText.ifEmpty { stringResource(R.string.example_speech_to_text) }
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SSJetPackComposeProgressButton(
                        type = SSButtonType.CIRCLE,
                        width = 157.dp,
                        height = 50.dp,
                        buttonBorderColor = primaryColor,
                        buttonBorderWidth = 2.dp,
                        buttonState = retryRecordStateButton,
                        onClick = {
                            coroutineScope.launch {
                                retryRecordStateButton = SSButtonState.LOADING
                                delay(1000)
                                retryRecordStateButton = SSButtonState.IDLE
                                onNavigateRecordScreen()
                            }
                        },
                        cornerRadius = 100,
                        assetColor = primaryColor,
                        successIconPainter = null,
                        failureIconPainter = null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = whiteBackground,
                            contentColor = primaryColor
                        ),
                        text = stringResource(R.string.button_retry_record),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold
                    )

                    SSJetPackComposeProgressButton(
                        type = SSButtonType.CIRCLE,
                        width = 157.dp,
                        height = 50.dp,
                        buttonState = analyzeStateButton,
                        onClick = {
                            coroutineScope.launch {
                                analyzeStateButton = SSButtonState.LOADING
                                delay(1000)
                                analyzeStateButton = SSButtonState.IDLE
                                onNavigateAnalyzeScreen()
                            }
                        },
                        cornerRadius = 100,
                        assetColor = whiteColor,
                        successIconPainter = null,
                        failureIconPainter = null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = whiteColor,
                            disabledContainerColor = primaryColor,
                        ),
                        text = stringResource(R.string.button_analyze_grammar),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpeechToTextScreenPreview() {
    UrVoiceTheme {
        SpeechToTextScreen(
            onBackClick = {}
        )
    }
}