package com.firman.capstone.urvoice.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.firman.capstone.urvoice.R
import com.firman.capstone.urvoice.data.remote.models.ArticleResponse
import com.firman.capstone.urvoice.data.remote.models.CurrentUserResponse
import com.firman.capstone.urvoice.ui.components.ProfileCardContent
import com.firman.capstone.urvoice.ui.theme.PoppinsSemiBold
import com.firman.capstone.urvoice.ui.theme.primaryColor
import com.firman.capstone.urvoice.ui.theme.whiteColor
import com.firman.capstone.urvoice.ui.viewmodel.ProfileViewModel
import com.firman.capstone.urvoice.utils.ResultState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    onLanguageClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.menu_profile_title),
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ProfileCard(
                viewModel = viewModel,
                onEditClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pengaturan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SettingsItem(
                icon = Icons.Default.Delete,
                title = "Bahasa",
                onClick = onLanguageClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingsItem(
                icon = Icons.Default.Delete,
                title = "Hapus Akun",
                onClick = {
                    onDeleteAccountClick()
                    viewModel.deleteUser()
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSignOutClick()
                    viewModel.logout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5252)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Sign out",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProfileCard(
    viewModel: ProfileViewModel,
    onEditClick: () -> Unit
) {
    val userProfileState = viewModel.currentUserProfile.collectAsState()

    when (val state = userProfileState.value) {
        is ResultState.Loading -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val composition = rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.loading_animation)
                    ).value

                    if (composition != null) {
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }
        }

        is ResultState.Success -> {
            ProfileCardContent(
                name = state.data.data?.name ?: "Unknown User",
                email = state.data.data?.email ?: "No email",
                profileImage = state.data.data?.profileImage.toString(),
                onEditClick = onEditClick
            )
        }

        is ResultState.Error -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        }

        else -> Unit
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Arrow",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}