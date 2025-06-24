package com.firman.capstone.urvoice.ui.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.firman.capstone.urvoice.ui.viewmodel.SpeechViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechToTextScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeechViewModel = hiltViewModel()
) {
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
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        SpeechToTextCard(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 15.dp),
            text = convertedText.ifEmpty { "Ini adalah konten kartu untuk fitur Speech to Text." }.toString()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpeechToTextScreenPreview() {
    UrVoiceTheme {
        SpeechToTextScreen()
    }
}