package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.wesleyaldrich.pancook.ui.theme.inter
import com.wesleyaldrich.pancook.ui.theme.montserrat
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins
import org.w3c.dom.Text

@Composable
fun CenteredTextBox(text: String, family: FontFamily, weight: FontWeight = FontWeight.Normal, size: TextUnit, align: TextAlign) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontFamily = family, fontWeight = weight, fontSize = size, textAlign = align)
    }
}

@Composable
fun PlannerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Planner Screen Montserrat",
            textAlign = TextAlign.Center,
            fontFamily = montserrat, // make sure this is correct
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        Text(
            text = "Planner Screen Poppins",
            textAlign = TextAlign.Center,
            fontFamily = poppins, // make sure this is correct
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        Text(
            text = "Planner Screen Nunito",
            fontFamily = nunito, // make sure this is correct
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PlannerPagePreview () {
    PlannerScreen()
}