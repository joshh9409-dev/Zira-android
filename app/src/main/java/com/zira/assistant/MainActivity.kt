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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

private val Space = Color(0xFF050008)
private val Purple = Color(0xFFB967FF)
private val Bright = Color(0xFFF1D9FF)
private val Violet = Color(0xFF7020B5)
private val Deep = Color(0xFF160020)

enum class ZiraState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING,
    PERMISSION,
    SUCCESS,
    ERROR
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZiraApp()
        }
    }
}

@Composable
fun ZiraApp() {

    var state by remember {
        mutableStateOf(ZiraState.IDLE)
    }

    val message = when (state) {
        ZiraState.IDLE ->
            "Online, sir. What do you need, Josh?"

        ZiraState.LISTENING ->
            "I'm listening, sir."

        ZiraState.THINKING ->
            "One moment, Josh..."

        ZiraState.SPEAKING ->
            "Right away, sir."

        ZiraState.PERMISSION ->
            "Permission required, Josh. Proceed?"

        ZiraState.SUCCESS ->
            "Done, sir."

        ZiraState.ERROR ->
            "Something went wrong, Josh."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF180025),
                        Space,
                        Color(0xFF020003)
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
                color = Bright,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = state.name,
                color = Purple,
                fontSize = 12.sp,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

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
                color = Bright,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ZiraButton(
                    text = "MIC",
                    onClick = {
                        state =
                            if (state == ZiraState.LISTENING)
                                ZiraState.IDLE
                            else
                                ZiraState.LISTENING
                    }
                )

                ZiraButton(
                    text = "CHAT",
                    onClick = {
                        state = ZiraState.SPEAKING
                    }
                )

                ZiraButton(
                    text = "MENU",
                    onClick = {
                        state = ZiraState.PERMISSION
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when (state) {
                    ZiraState.IDLE -> "READY • STANDING BY"
                    ZiraState.LISTENING -> "LISTENING • MICROPHONE ACTIVE"
                    ZiraState.THINKING -> "PROCESSING • THINKING"
                    ZiraState.SPEAKING -> "SPEAKING • ZIRA ACTIVE"
                    ZiraState.PERMISSION -> "AWAITING PERMISSION"
                    ZiraState.SUCCESS -> "TASK COMPLETE"
                    ZiraState.ERROR -> "SYSTEM ATTENTION REQUIRED"
                },
                color = Purple.copy(alpha = 0.8f),
                fontSize = 10.sp,
                letterSpacing = 1.4.sp
            )
        }
    }
}

@Composable
private fun ZiraOrb(state: ZiraState) {

    val transition = rememberInfiniteTransition(label = "zira_animation")

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(9000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulse by transition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            tween(
                when (state) {
                    ZiraState.LISTENING -> 650
                    ZiraState.THINKING -> 400
                    ZiraState.SPEAKING -> 300
                    ZiraState.SUCCESS -> 220
                    ZiraState.ERROR -> 180
                    else -> 1500
                }
            ),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val movement by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(
                when (state) {
                    ZiraState.THINKING -> 500
                    ZiraState.SPEAKING -> 350
                    else -> 1800
                }
            ),
            RepeatMode.Reverse
        ),
        label = "movement"
    )

    Canvas(
        modifier = Modifier.size(365.dp)
    ) {

        val cx = size.width / 2f
        val cy = size.height / 2f

        val radius = size.minDimension * 0.34f
        val intensity = when (state) {
            ZiraState.IDLE -> 0.75f
            ZiraState.LISTENING -> 1.15f
            ZiraState.THINKING -> 1.35f
            ZiraState.SPEAKING -> 1.45f
            ZiraState.PERMISSION -> 1.05f
            ZiraState.SUCCESS -> 
