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

private val Space = Color(0xFF050008)
private val Purple = Color(0xFFB967FF)
private val Bright = Color(0xFFF1D9FF)
private val Deep = Color(0xFF160020)

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
        ZiraState.THINKING -> "One moment, Josh..."
        ZiraState.SPEAKING -> "Right away, sir."
        ZiraState.PERMISSION -> "Permission required, Josh. Proceed?"
        ZiraState.SUCCESS -> "Done, sir."
        ZiraState.ERROR -> "Something went wrong, Josh."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF180025),
                        Space,
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
                color = Bright,
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
private fun ZiraButton(
    text: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(92.dp, 46.dp)
            .clip(RoundedCornerShape(23.dp))
            .background(Color(0xFF190027))
            .border(
                1.dp,
                Purple.copy(alpha = 0.8f),
                RoundedCornerShape(23.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Bright,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun ZiraOrb(state: ZiraState) {

    val transition = rememberInfiniteTransition(
        label = "zira"
    )

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                9000,
                easing = LinearEasing
            )
        ),
        label = "rotation"
    )

    val pulse by transition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                if (state == ZiraState.SPEAKING) {
                    300
                } else if (state == ZiraState.LISTENING) {
                    600
                } else {
                    1400
                }
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val movement by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                if (state == ZiraState.THINKING) {
                    500
                } else {
                    1800
                }
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "movement"
    )

    Canvas(
        modifier = Modifier.size(365.dp)
    ) {

        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = size.minDimension * 0.34f

        val activeColor =
            if (state == ZiraState.ERROR) {
                Purple
            } else {
                Bright
            }

        val intensity =
            if (state == ZiraState.IDLE) {
                0.7f
            } else {
                1.15f
            }

        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Purple.copy(alpha = 0.32f * intensity),
                    Purple.copy(alpha = 0.1f),
                    Color.Transparent
                )
            ),
            radius = radius * 1.65f,
            center = Offset(cx, cy)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Color(0xFF7020B5),
                    Deep,
                    Color(0xFF08000C)
                )
            ),
            radius = radius,
            center = Offset(cx, cy)
        )

        drawCircle(
            color = Purple.copy(alpha = 0.9f),
            radius = radius * pulse,
            center = Offset(cx, cy),
            style = Stroke(3f)
        )

        drawCircle(
            color = Bright.copy(alpha = 0.25f),
            radius = radius * 0.86f,
            center = Offset(cx, cy),
            style = Stroke(1.5f)
        )

        drawOval(
            color = Purple.copy(alpha = 0.7f),
            topLeft = Offset(
                cx - radius * 1.18f,
                cy - radius * 0.42f
            ),
            size = androidx.compose.ui.geometry.Size(
                radius * 2.36f,
                radius * 0.84f
            ),
            style = Stroke(2f)
        )

        val angle = Math.toRadians(
            rotation.toDouble()
        )

        drawCircle(
            color = Bright,
            radius = 5f * pulse,
            center = Offset(
                cx + cos(angle).toFloat() * radius * 1.2f,
                cy + sin(angle).toFloat() * radius * 0.42f
            )
        )

        if (state == ZiraState.LISTENING) {

            drawCircle(
                color = Bright.copy(alpha = 0.45f),
                radius = radius * 1.25f * pulse,
                center = Offset(cx, cy),
                style = Stroke(2f)
            )

            drawLine(
                color = Bright.copy(alpha = 0.65f),
                start = Offset(
                    cx - radius,
                    cy + movement * 45f
                ),
                end = Offset(
                    cx + radius,
                    cy + movement * 45f
                ),
                strokeWidth = 2f
            )
        }

        if (state == ZiraState.THINKING) {

            for (i in 0..2) {

                val particleAngle =
                    angle + i * 2.1

                drawCircle(
                    color = Bright,
                    radius = 4f,
                    center = Offset(
                        cx + cos(particleAngle).toFloat() *
                            radius * 1.35f,
                        cy + sin(particleAngle).toFloat() *
                            radius * 1.35f
                    )
                )
            }
        }

        if (state == ZiraState.SUCCESS) {

            drawCircle(
                color = Bright.copy(alpha = 0.7f),
                radius = radius * 1.3f * pulse,
                center = Offset(cx, cy),
                style = Stroke(4f)
            )
        }

        if (state == ZiraState.ERROR) {

            drawCircle(
                color = Purple,
                radius = radius * (1.12f + pulse * 0.1f),
                center = Offset(cx, cy),
                style = Stroke(4f)
            )
        }

        drawAvatar(
            center = Offset(
                cx,
                cy + movement * 4f
            ),
            scale = radius / 100f,
            state = state,
            pulse = pulse,
            glow = activeColor
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAvatar(
    center: Offset,
    scale: Float,
    state: ZiraState,
    pulse: Float,
    glow: Color
) {

    val head = Offset(
        center.x,
        center.y - 48f * scale
    )

    val lineColor = glow.copy(alpha = 0.85f)

    drawCircle(
        color = Purple.copy(alpha = 0.2f),
        radius = 25f * scale * pulse,
        center = head
    )

    drawCircle(
        color = lineColor,
        radius = 13f * scale,
        center = head,
        style = Stroke(2.5f * scale)
    )

    val hair = Path().apply {

        moveTo(
            head.x - 13f * scale,
            head.y - 8f * scale
        )

        quadraticTo(
            head.x - 29f * scale,
            head.y + 5f * scale,
            head.x - 19f * scale,
            head.y + 27f * scale
        )

        quadraticTo(
            head.x - 8f * scale,
            head.y + 13f * scale,
            head.x,
            head.y + 7f * scale
        )

        quadraticTo(
            head.x + 8f * scale,
            head.y + 13f * scale,
            head.x + 19f * scale,
            head.y + 27f * scale
        )

        quadraticTo(
            head.x + 29f * scale,
            head.y + 5f * scale,
            head.x + 13f * scale,
            head.y - 8f * scale
        )
    }

    drawPath(
        path = hair,
        color = Purple.copy(alpha = 0.75f),
        style = Stroke(3f * scale)
    )

    val body = Path().apply {

        moveTo(
            center.x - 10f * scale,
            center.y - 34f * scale
        )

        cubicTo(
            center.x - 22f * scale,
            center.y - 20f * scale,
            center.x - 22f * scale,
            center.y + 3f * scale,
            center.x - 15f * scale,
            center.y + 20f * scale
        )

        cubicTo(
            center.x - 11f * scale,
            center.y + 35f * scale,
            center.x - 10f * scale,
            center.y + 45f * scale,
            center.x - 13f * scale,
            center.y + 58f * scale
        )

        moveTo(
            center.x
