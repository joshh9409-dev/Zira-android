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
import androidx.compose.ui.graphics.*
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

private val Purple = Color(0xFFB967FF)
private val LightPurple = Color(0xFFF0D7FF)
private val DarkPurple = Color(0xFF160020)
private val Black = Color(0xFF050008)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZiraApp()
        }
    }
}

@Composable
private fun ZiraApp() {

    var state by remember {
        mutableStateOf(ZiraState.IDLE)
    }

    val message = when (state) {
        ZiraState.IDLE -> "Online, sir. What do you need, Josh?"
        ZiraState.LISTENING -> "I'm listening, sir."
        ZiraState.THINKING -> "Thinking..."
        ZiraState.SPEAKING -> "Right away, sir."
        ZiraState.PERMISSION -> "Permission required, Josh."
        ZiraState.SUCCESS -> "Done, sir."
        ZiraState.ERROR -> "Something went wrong."
    }

    val status = when (state) {
        ZiraState.IDLE -> "READY • STANDING BY"
        ZiraState.LISTENING -> "LISTENING"
        ZiraState.THINKING -> "THINKING"
        ZiraState.SPEAKING -> "SPEAKING"
        ZiraState.PERMISSION -> "AWAITING PERMISSION"
        ZiraState.SUCCESS -> "TASK COMPLETE"
        ZiraState.ERROR -> "SYSTEM ERROR"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF190027),
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
                color = LightPurple,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = state.name,
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
                color = LightPurple,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ZiraButton("MIC") {
                    state = if (state == ZiraState.LISTENING) {
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
                text = status,
                color = Purple.copy(alpha = 0.8f),
                fontSize = 10.sp,
                letterSpacing = 1.5.sp
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
            .size(92.dp, 46.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF190027))
            .border(
                1.dp,
                Purple,
                RoundedCornerShape(24.dp)
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
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ZiraOrb(state: ZiraState) {

    val animation = rememberInfiniteTransition(
        label = "zira"
    )

    val rotation by animation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                8000,
                easing = LinearEasing
            )
        ),
        label = "rotation"
    )

    val pulse by animation.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                if (state == ZiraState.SPEAKING) 300
                else if (state == ZiraState.LISTENING) 600
                else 1400
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val floatY by animation.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Canvas(
        modifier = Modifier.size(350.dp)
    ) {

        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radius = size.minDimension * 0.34f

        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Purple.copy(alpha = 0.4f),
                    Purple.copy(alpha = 0.12f),
                    Color.Transparent
                )
            ),
            radius = radius * 1.6f,
            center = Offset(centerX, centerY)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Color(0xFF7624BE),
                    DarkPurple,
                    Black
                )
            ),
            radius = radius,
            center = Offset(centerX, centerY)
        )

        drawCircle(
            color = Purple,
            radius = radius * pulse,
            center = Offset(centerX, centerY),
            style = Stroke(3f)
        )

        drawCircle(
            color = LightPurple.copy(alpha = 0.3f),
            radius = radius * 0.86f,
            center = Offset(centerX, centerY),
            style = Stroke(1.5f)
        )

        drawOval(
            color = Purple.copy(alpha = 0.75f),
            topLeft = Offset(
                centerX - radius * 1.2f,
                centerY - radius * 0.4f
            ),
            size = androidx.compose.ui.geometry.Size(
                radius * 2.4f,
                radius * 0.8f
            ),
            style = Stroke(2f)
        )

        val angle = Math.toRadians(
            rotation.toDouble()
        )

        drawCircle(
            color = LightPurple,
            radius = 5f,
            center = Offset(
                centerX + cos(angle).toFloat() * radius * 1.2f,
                centerY + sin(angle).toFloat() * radius * 0.4f
            )
        )

        if (state == ZiraState.LISTENING) {

            drawCircle(
                color = LightPurple.copy(alpha = 0.5f),
                radius = radius * 1.25f * pulse,
                center = Offset(centerX, centerY),
                style = Stroke(2f)
            )

            drawLine(
                color = LightPurple.copy(alpha = 0.7f),
                start = Offset(
                    centerX - radius,
                    centerY + floatY * 8f
                ),
                end = Offset(
                    centerX + radius,
                    centerY + floatY * 8f
                ),
                strokeWidth = 2f
            )
        }

        if (state == ZiraState.THINKING) {

            for (i in 0..2) {

                val a = angle + i * 2.1

                drawCircle(
                    color = LightPurple,
                    radius = 4f,
                    center = Offset(
                        centerX + cos(a).toFloat() * radius * 1.35f,
                        centerY + sin(a).toFloat() * radius * 1.35f
                    )
                )
            }
        }

        if (state == ZiraState.SUCCESS) {

            drawCircle(
                color = LightPurple.copy(alpha = 0.7f),
                radius = radius * 1.3f * pulse,
                center = Offset(centerX, centerY),
                style = Stroke(4f)
            )
        }

        if (state == ZiraState.ERROR) {

            drawCircle(
                color = Purple,
                radius = radius * 1.2f,
                center = Offset(centerX, centerY),
                style = Stroke(4f)
            )
        }

        drawAvatar(
            Offset(
                centerX,
                centerY + floatY
            ),
            radius / 100f,
            state,
            pulse
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAvatar(
    center: Offset,
    scale: Float,
    state: ZiraState,
    pulse: Float
) {

    val head = Offset(
        center.x,
        center.y - 45f * scale
    )

    val glow = LightPurple.copy(alpha = 0.85f)

    drawCircle(
        color = Purple.copy(alpha = 0.2f),
        radius = 24f * scale * pulse,
        center = head
    )

    drawCircle(
        color = glow,
        radius = 12f * scale,
        center = head,
        style = Stroke(2.5f * scale)
    )

    val hair = Path().apply {

        moveTo(
            head
