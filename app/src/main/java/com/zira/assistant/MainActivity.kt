package com.zira.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZiraScreen()
        }
    }
}

@androidx.compose.runtime.Composable
fun ZiraScreen() {

    val infiniteTransition = rememberInfiniteTransition(label = "ziraPulse")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val glow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF050008),
                        Color(0xFF10001C),
                        Color(0xFF050008)
                    )
                )
            )
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ZIRA",
                color = Color(0xFFE0B0FF),
                fontSize = 38.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp
            )

            Text(
                text = "ONLINE",
                color = Color(0xFFB96CFF),
                fontSize = 12.sp,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.size(250.dp),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .scale(pulse)
                        .alpha(glow)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFFB84DFF),
                                    Color(0x664D00FF),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(165.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFFE5B5FF),
                                    Color(0xFF9C27FF),
                                    Color(0xFF3B0066)
                                )
                            ),
                            CircleShape
                        )
                        .border(
                            2.dp,
                            Color(0xFFD889FF),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                Color.White.copy(alpha = 0.18f),
                                CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Online, sir.",
                color = Color(0xFFE7D4FF),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "What do you need, Josh?",
                color = Color(0xFFBCA6C9),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ZiraButton(
                    text = "MIC",
                    modifier = Modifier.weight(1f)
                )

                ZiraButton(
                    text = "CHAT",
                    modifier = Modifier.weight(1f)
                )

                ZiraButton(
                    text = "MENU",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "READY • LISTENING FOR COMMAND",
                color = Color(0xFF8E6AA3),
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@androidx.compose.runtime.Composable
fun ZiraButton(
    text: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(52.dp)
            .background(
                Color(0x331B002B),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                Color(0xFF7E35A6),
                RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Color(0xFFD8A5FF),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp
        )
    }
}
