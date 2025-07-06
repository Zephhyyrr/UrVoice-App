package com.firman.capstone.urvoice.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firman.capstone.urvoice.data.remote.models.HistoryResponse
import com.firman.capstone.urvoice.ui.theme.*

@Composable
fun HistoryDetailAnalysisCard(
    grammar: HistoryResponse.GrammarAnalysis,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = whiteColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Grammar Analysis:",
                fontSize = 14.sp,
                fontFamily = PoppinsSemiBold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            AnalysisItem(label = "Original", text = grammar.original)
            Spacer(modifier = Modifier.height(8.dp))

            AnalysisItem(label = "Corrected", text = grammar.corrected, highlight = true)
            Spacer(modifier = Modifier.height(8.dp))

            AnalysisItem(label = "Reason", text = grammar.reason)
        }
    }
}

@Composable
private fun AnalysisItem(
    label: String,
    text: String?,
    highlight: Boolean = false
) {
    Text(
        text = "$label:",
        fontSize = 12.sp,
        color = Color.Gray,
        fontFamily = PoppinsMedium,
    )
    Text(
        text = text ?: "No $label provided",
        fontSize = 14.sp,
        fontFamily = PoppinsRegular,
        color = if (highlight) primaryColor else Color.Black,
        modifier = Modifier.padding(top = 2.dp)
    )
}
