package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.wesleyaldrich.pancook.ui.theme.inter
import com.wesleyaldrich.pancook.ui.theme.montserrat
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins

@Composable
fun PlannerScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Planner Screen", fontFamily = inter, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PlannerPagePreview () {
    PlannerScreen()
}