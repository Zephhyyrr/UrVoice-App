package com.firman.capstone.urvoice.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.firman.capstone.urvoice.R
import com.firman.capstone.urvoice.data.remote.models.ArticleResponse
import com.firman.capstone.urvoice.data.remote.models.CurrentUserResponse
import com.firman.capstone.urvoice.ui.components.CardHomeArticle
import com.firman.capstone.urvoice.ui.theme.PoppinsSemiBold
import com.firman.capstone.urvoice.ui.theme.primaryColor
import com.firman.capstone.urvoice.ui.theme.textColor
import com.firman.capstone.urvoice.ui.theme.whiteBackground
import com.firman.capstone.urvoice.ui.viewmodel.HomeViewModel
import com.firman.capstone.urvoice.utils.ResultState

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val articlesState by viewModel.articlesState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(primaryColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 16.dp)
            ) {
                UserHeaderContent(userState)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 178.dp),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            colors = CardDefaults.cardColors(containerColor = whiteBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                SectionTitle(stringResource(R.string.article_home_title))
                ArticlesContent(articlesState, navController, viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                SectionTitle(
                    stringResource(
                        R.string.update_history_title_home
                    )
                )
            }
        }
    }
}

@Composable
private fun UserHeaderContent(userState: ResultState<*>) {
    when (userState) {
        is ResultState.Success -> UserHeader((userState as ResultState.Success<CurrentUserResponse>).data)
        is ResultState.Error -> UserHeaderError()
        else -> UserHeaderSkeleton()
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            fontFamily = PoppinsSemiBold,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun ArticlesContent(
    articlesState: ResultState<*>,
    navController: NavController,
    viewModel: HomeViewModel
) {
    when (articlesState) {
        is ResultState.Success -> {
            val articles =
                (articlesState as ResultState.Success<List<ArticleResponse.Data>>).data.take(5)

            if (articles.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(articles) { article ->
                        CardHomeArticle(
                            imageUrl = article.image.orEmpty(),
                            onClick = {
                                navController.navigate("article/${article.id}")
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LottieAnimation(
                            modifier = Modifier.size(250.dp),
                            composition = rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.nodata_animation)
                            ).value,
                            iterations = LottieConstants.IterateForever,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = stringResource(R.string.nodata_article_title),
                            style = TextStyle(
                                color = textColor,
                                fontSize = 14.sp,
                                fontFamily = PoppinsSemiBold
                            )
                        )
                    }
                }
            }
        }

        is ResultState.Loading -> {
            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation)).value,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        }

        is ResultState.Error -> {
            ArticlesListError(
                onRetry = viewModel::getAllArticles,
                error = articlesState.toString()
            )
        }

        else -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(3) { ArticleCardSkeleton() }
            }
        }
    }
}

@Composable
private fun UserHeader(user: CurrentUserResponse) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileIcon()
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Hello, ${user.data?.name ?: "User"}",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = PoppinsSemiBold,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        )
    }
}

@Composable
private fun UserHeaderSkeleton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(20.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun UserHeaderError() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileIcon()
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Hello, User",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = PoppinsSemiBold,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        )
    }
}

@Composable
private fun ProfileIcon() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ArticleCardSkeleton() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.size(width = 327.dp, height = 180.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun ArticlesListError(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Failed to load articles",
            style = TextStyle(fontSize = 14.sp, color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text("Retry", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavController(LocalContext.current), viewModel = hiltViewModel())
}