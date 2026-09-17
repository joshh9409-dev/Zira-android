package com.zira.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

enum class ZiraState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING,
    PERMISSION,
    SUCCESS,
    ERROR
}

private val Black = Color(0xFF030006)
private val Purple = Color(0xFFB967FF)
private val Light = Color(0xFFF1D9FF)
private val Violet = Color(0xFF6E20B5)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZiraScreen()
        }
    }
}

@Composable
private fun ZiraScreen() {

    var state by remember {
        mutableStateOf(ZiraState.IDLE)
    }

    val message = when (state) {
        ZiraState.IDLE -> "Online, sir. What do you need, Josh?"
        ZiraState.LISTENING -> "I'm listening, sir."
        ZiraState.THINKING -> "Processing, Josh..."
        ZiraState.SPEAKING -> "Right away, sir."
        ZiraState.PERMISSION -> "Permission required, Josh."
        ZiraState.SUCCESS -> "Task complete, sir."
        ZiraState.ERROR -> "Something went wrong, Josh."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF19002A),
                        Black,
                        Color.Black
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "ZIRA",
                color = Light,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "ONLINE",
                color = Purple,
                fontSize = 12.sp,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ZiraOrb(state)
            }

            Text(
                text = message,
                color = Light,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ZiraButton("MIC") {
                    state =
                        if (state == ZiraState.LISTENING) {
                            ZiraState.IDLE
                        } else {
                            ZiraState.LISTENING
                        }
                }

                ZiraButton("CHAT") {
                    state = ZiraState.SPEAKING
                }

                ZiraButton("MENU") {
                    state = ZiraState.PERMISSION
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = statusText(state),
                color = Purple.copy(alpha = 0.8f),
                fontSize = 10.sp,
                letterSpacing = 1.5.sp
            )
        }
    }
}

private fun statusText(state: ZiraState): String {

    return when (state) {
        ZiraState.IDLE -> "READY • STANDING BY"
        ZiraState.LISTENING -> "LISTENING • MICROPHONE"
        ZiraState.THINKING -> "THINKING • PROCESSING"
        ZiraState.SPEAKING -> "SPEAKING • ZIRA ACTIVE"
        ZiraState.PERMISSION -> "AWAITING PERMISSION"
        ZiraState.SUCCESS -> "TASK COMPLETE"
        ZiraState.ERROR -> "SYSTEM ATTENTION"
    }
}

@Composable
private fun ZiraButton(
    text:
