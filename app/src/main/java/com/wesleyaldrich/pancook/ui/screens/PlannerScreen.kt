package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.wesleyaldrich.pancook.R
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
            .fillMaxSize()
            .padding(
                20.dp, 25.dp
            )
    ) {
        PlanItem("Hash Brown", 4)
    }
}

@Composable
fun DateSectionItem() {
    
}

@Composable
fun PlanItem(title :String, serveCount : Int) {
    Box(
        modifier = Modifier
            .padding(12.dp, 8.dp)
            .background(
                color = colorResource(id = R.color.primary),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hash_brown),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                Text(
                    text = title,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(0.dp))
                Text(
                    text = "$serveCount " +
                            "people",
                    fontFamily = nunito,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row (
                modifier = Modifier
            ) {

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PlannerPagePreview () {
//    PlannerScreen()
}