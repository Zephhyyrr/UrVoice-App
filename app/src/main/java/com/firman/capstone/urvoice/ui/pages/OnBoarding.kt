package com.firman.capstone.urvoice.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.firman.capstone.urvoice.R
import com.firman.capstone.urvoice.ui.theme.PoppinsRegular
import com.firman.capstone.urvoice.ui.theme.PoppinsSemiBold
import com.firman.capstone.urvoice.ui.theme.UrVoiceTheme
import com.firman.capstone.urvoice.ui.theme.buttonShape
import com.firman.capstone.urvoice.ui.theme.primaryColor
import com.firman.capstone.urvoice.ui.theme.textColor
import com.firman.capstone.urvoice.ui.theme.whiteBackground
import com.firman.capstone.urvoice.ui.theme.whiteColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSCustomLoadingEffect
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class OnBoardModel(
    val imageRes: Int,
    val title: String,
    val description: String
)

val onBoardModel = listOf(
    OnBoardModel(
        title = "Selamat Datang di\nUrVoice",
        description = "Kami menyediakan speech-to-text dalam Bahasa Inggris dan koreksi grammar yang membantu Anda meningkatkan kemampuan public speaking dalam Bahasa Inggris",
        imageRes = R.drawable.iv_onboarding_1
    ),
    OnBoardModel(
        title = "Aplikasi Untuk Menganalisis Speaking Anda",
        description = "Analisis speaking Anda dengan menggunakan teknologi Generative AI",
        imageRes = R.drawable.iv_onboarding_2
    ),
    OnBoardModel(
        title = "Ayo Tingkatkan Percaya Diri Bicara Bahasa Inggris Anda",
        description = "Ayo Mulai Sekarang dan tingkatkan percaya diri Anda dalam berbicara Bahasa Inggris",
        imageRes = R.drawable.iv_onboarding_3
    )
)

@Composable
fun OnBoardItem(page: OnBoardModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .height(350.dp)
                .width(350.dp)
        )
        Text(
            text = page.title,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 19.dp),
            style = TextStyle(
                fontFamily = PoppinsSemiBold,
                fontSize = 20.sp,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = 15.dp),
            style = TextStyle(
                fontFamily = PoppinsRegular,
                fontSize = 12.sp,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ButtonRow(
    pagerState: PagerState,
    onFinishOnboarding: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var nextButtonState by remember { mutableStateOf(SSButtonState.IDLE) }
    var backButtonState by remember { mutableStateOf(SSButtonState.IDLE) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 24.dp)
    ) {
        if (pagerState.currentPage == onBoardModel.size - 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SSJetPackComposeProgressButton(
                    type = SSButtonType.CUSTOM,
                    width = 380.dp,
                    height = 50.dp,
                    buttonState = nextButtonState,
                    onClick = {
                        coroutineScope.launch {
                            nextButtonState = SSButtonState.LOADING
                            delay(1000)
                            nextButtonState = SSButtonState.SUCCESS
                            delay(1000)
                            onFinishOnboarding()
                        }
                    },
                    cornerRadius = 100,
                    assetColor = Color.White,
                    successIconPainter = null,
                    failureIconPainter = null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = whiteColor,
                        disabledContainerColor = primaryColor,
                    ),
                    text = "Mulai Sekarang",
                    textModifier = Modifier,
                    fontSize = 14.sp,
                    fontFamily = PoppinsSemiBold
                )
            }
        } else if (pagerState.currentPage > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SSJetPackComposeProgressButton(
                    type = SSButtonType.CIRCLE,
                    width = 157.dp,
                    height = 50.dp,
                    buttonBorderColor = primaryColor,
                    buttonBorderWidth = 2.dp,
                    buttonState = backButtonState,
                    onClick = {
                        coroutineScope.launch {
                            backButtonState = SSButtonState.LOADING
                            delay(1000)
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            backButtonState = SSButtonState.IDLE
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
                    text = "Kembali",
                    textModifier = Modifier,
                    fontSize = 14.sp,
                    fontFamily = PoppinsSemiBold
                )

                SSJetPackComposeProgressButton(
                    type = SSButtonType.CIRCLE,
                    width = 157.dp,
                    height = 50.dp,
                    buttonState = nextButtonState,
                    onClick = {
                        coroutineScope.launch {
                            nextButtonState = SSButtonState.LOADING
                            delay(1000)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            nextButtonState = SSButtonState.IDLE
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
                    text = "Selanjutnya",
                    textModifier = Modifier,
                    fontSize = 14.sp,
                    fontFamily = PoppinsSemiBold
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SSJetPackComposeProgressButton(
                    type = SSButtonType.CIRCLE,
                    width = 380.dp,
                    height = 50.dp,
                    buttonState = nextButtonState,
                    onClick = {
                        coroutineScope.launch {
                            nextButtonState = SSButtonState.LOADING
                            delay(1000)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            nextButtonState = SSButtonState.IDLE
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
                    text = "Selanjutnya",
                    textModifier = Modifier,
                    fontSize = 14.sp,
                    fontFamily = PoppinsSemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    onFinishOnboarding: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = onBoardModel.size,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) { page ->
            OnBoardItem(page = onBoardModel[page])
        }
        Row(
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(onBoardModel.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .width(if (isSelected) 20.dp else 10.dp)
                        .height(10.dp)
                        .border(
                            width = 0.5.dp,
                            color = textColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(
                            color = if (isSelected) primaryColor else Color.White,
                            shape = CircleShape
                        )
                )
            }
        }
        ButtonRow(pagerState = pagerState, onFinishOnboarding = onFinishOnboarding)
    }
}

@Preview
@Composable
fun OnBoardingPreview() {
    UrVoiceTheme {
        OnBoardingScreen(
            navController = NavController(LocalContext.current),
            onFinishOnboarding = {}
        )
    }
}
