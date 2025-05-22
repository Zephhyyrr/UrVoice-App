package com.firman.capstone.urvoice.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.firman.capstone.urvoice.ui.theme.PoppinsSemiBold
import com.firman.capstone.urvoice.ui.theme.UrVoiceTheme
import com.firman.capstone.urvoice.ui.theme.textColor

@Composable
fun HomeScreen(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Ini Home",
            style = TextStyle(
                fontFamily = PoppinsSemiBold,
                fontSize = 32.sp,
                color = textColor
            )
        )
    }
}
@Composable
@Preview(showBackground = true)
fun HomePreview(){
    UrVoiceTheme {
        HomeScreen(navController = NavController(LocalContext.current))
    }

}