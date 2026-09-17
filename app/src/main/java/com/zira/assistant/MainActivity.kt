package com.zira.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

enum class ZState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING,
    PERMISSION,
    SUCCESS,
    ERROR
}

private val Purple = Color(0xFFB967FF)
private val LightPurple = Color(0xFFF0D7FF)
private val DarkPurple = Color(0xFF190025)
private val Black = Color(0xFF050008)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Zira()
        }
    }
}

@Composable
private fun Zira() {

    var state by remember {
        mutableStateOf(ZState.IDLE)
    }

    val message = when (state) {
        ZState.IDLE ->
            "Online, sir. What do you need, Josh?"

        ZState.LISTENING ->
            "I'm listening, sir."

        ZState.THINKING ->
            "Thinking, Josh..."

        ZState.SPEAKING ->
            "Right away, sir."

        ZState.PERMISSION ->
            "Permission required, Josh."

        ZState.SUCCESS ->
            "Done, sir."

        ZState.ERROR ->
            "Something went wrong."
    }

    val status = when (state) {
        ZState.IDLE ->
            "READY • STANDING BY"

        ZState.LISTENING ->
            "LISTENING"

        ZState.THINKING ->
            "THINKING"

        ZState.SPEAKING ->
            "SPEAKING"

        ZState.PERMISSION ->
            "AWAITING PERMISSION"

        ZState.SUCCESS ->
            "TASK COMPLETE"

        ZState.ERROR ->
            "SYSTEM ERROR"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF190025),
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

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "ZIRA",
                color = LightPurple,
                fontSize = 32.sp
            )

            Text(
                text = "ONLINE",
                color = Purple,
                fontSize = 12.sp,
                letterSpacing = 4.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Orb(state)
            }

            Text(
                text = message,
                color = LightPurple,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ZiraButton("MIC") {

                    state =
                        if (state == ZState.LISTENING) {
                            ZState.IDLE
                        } else {
                            ZState.LISTENING
                        }
                }

                ZiraButton("THINK") {
                    state = ZState.THINKING
                }

                ZiraButton("MENU") {
                    state = ZState.PERMISSION
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = status,
                color = Purple.copy(alpha = 0.8f),
                fontSize = 10.sp,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
    }
}

@Composable
private fun ZiraButton(
    text: String,
    action: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(
                width = 92.dp,
                height = 46.dp
            )
            .clip(
                RoundedCornerShape(24.dp)
            )
            .background(
                DarkPurple
            )
            .border(
                width = 1.dp,
                color = Purple,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                action()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = LightPurple,
            fontSize = 11.sp,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun Orb(
    state: ZState
) {

    val animation =
        rememberInfiniteTransition(
            label = "zira_animation"
        )

    val rotation by animation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(
                    durationMillis = 8000,
                    easing = LinearEasing
                )
            ),
        label = "rotation"
    )

    val pulseDuration =
        when (state) {
            ZState.LISTENING -> 500
            ZState.THINKING -> 350
            ZState.SPEAKING -> 250
            ZState.SUCCESS -> 300
            ZState.ERROR -> 180
