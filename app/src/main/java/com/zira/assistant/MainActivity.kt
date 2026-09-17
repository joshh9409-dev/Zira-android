package com.zira.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZiraMain()
        }
    }
}

@Composable
private fun ZiraMain() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        androidx.compose.ui.graphics.Color(0xFF21002F),
                        androidx.compose.ui.graphics.Color.Black
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ZIRA",
                color = androidx.compose.ui.graphics.Color(0xFFF0D7FF),
                fontSize = 34.sp
            )

            Text(
                text = "ONLINE",
                color = androidx.compose.ui.graphics.Color(0xFFB967FF),
                fontSize = 11.sp,
                letterSpacing = 5.sp
            )

            Spacer(Modifier.height(12.dp))

            Image(
                painter = painterResource(
                    id = R.drawable.file_0000000096a881f4820a61c7eda09a66
                ),
                contentDescription = "Zira",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Online, sir. What do you need, Josh?",
                color = androidx.compose.ui.graphics.Color(0xFFF0D7FF),
                fontSize = 16.sp
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "READY • STANDING BY",
                color = androidx.compose.ui.graphics.Color(0xFFB967FF),
                fontSize = 10.sp,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}
