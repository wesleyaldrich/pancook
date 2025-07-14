package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wesleyaldrich.pancook.viewmodel.ProfileViewModel
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.nunito

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current

    val profile by viewModel.profileState.collectAsState()

    var nameField by remember { mutableStateOf(TextFieldValue(profile.name)) }
    var emailField by remember { mutableStateOf(TextFieldValue(profile.email)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    viewModel.updateProfilePicture(R.drawable.nopal)
                },
            contentAlignment = Alignment.Center
        ) {
            if (profile.profilePictureResId != null) {
                Image(
                    painter = painterResource(id = profile.profilePictureResId!!),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Icon",
                    modifier = Modifier.size(64.dp),
                    tint = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ){
            Text(
                text = "Username",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Start,
                fontFamily = nunito
            )
            OutlinedTextField(
                value = nameField,
                onValueChange = { nameField = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Input username here") },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = nunito,
                    fontSize = 16.sp
                )
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Email",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Start,
                fontFamily = nunito
            )
            OutlinedTextField(
                value = emailField,
                onValueChange = { emailField = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Input email here") },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = nunito,
                    fontSize = 16.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                focusManager.clearFocus(force = true)

                viewModel.updateName(nameField.text)
                viewModel.updateEmail(emailField.text)
                viewModel.saveProfile()
            },
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Save",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = nunito
            )
        }
    }
}
