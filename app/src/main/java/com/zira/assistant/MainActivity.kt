package com.zira.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZiraScreen()
        }
    }
}

@Composable
fun ZiraScreen() {

    val transition = rememberInfiniteTransition(label = "zira")

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(9000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val pulse by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            tween(1800),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050008))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(28.dp))

            Text(
                text = "ZIRA",
                color = Color(0xFFE0AAFF),
                fontSize = 38.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp
            )

            Text(
                text = "HOLOGRAPHIC CORE",
                color = Color(0xFF9D5AC7),
                fontSize = 10.sp,
                letterSpacing = 3.sp
            )

            Spacer(Modifier.weight(1f))

            HologramOrb(
                rotation = rotation,
                pulse = pulse
            )

            Spacer(Modifier.height(25.dp))

            Text(
                text = "Online, sir.",
                color = Color(0xFFE7D5FF),
                fontSize = 23.sp
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "What do you need, Josh?",
                color = Color(0xFFB9A2C7),
                fontSize = 15.sp
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ZiraButton("MIC", Modifier.weight(1f))
                ZiraButton("CHAT", Modifier.weight(1f))
                ZiraButton("MENU", Modifier.weight(1f))
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "READY • ZIRA ONLINE",
                color = Color(0xFF765487),
                fontSize = 10.sp,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(15.dp))
        }
    }
}

@Composable
fun HologramOrb(
    rotation: Float,
    pulse: Float
) {

    Box(
        modifier = Modifier
            .size(310.dp)
            .scale(pulse),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension * 0.42f

            // Outer atmospheric glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xAA9B27FF),
                        Color(0x553C0870),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.8f
                ),
                radius = radius * 1.8f,
                center = center
            )

            // Main glass sphere
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE0A8FF),
                        Color(0xFF9C27FF),
                        Color(0xFF3A0068),
                        Color(0xFF12001F)
                    ),
                    center = Offset(
                        center.x - radius * .25f,
                        center.y - radius * .3f
                    ),
                    radius = radius * 1.4f
                ),
                radius = radius,
                center = center
            )

            // Glass edge
            drawCircle(
                color = Color(0xFFD891FF),
                radius = radius,
                center = center,
                style = Stroke(
                    width = 3.dp.toPx()
                )
            )

            // Rotating holographic rings
            val ringRadius = radius * 0.82f

            for (i in 0..2) {

                val angle = Math.toRadians(
                    (rotation + i * 120).toDouble()
                )

                val x = center.x + cos(angle).toFloat() * ringRadius
                val y = center.y + sin(angle).toFloat() * ringRadius

                drawCircle(
                    color = Color(0x99E1A4FF),
                    radius = 8.dp.toPx(),
                    center = Offset(x, y)
                )
            }

            // Horizontal scan lines
            for (i in -5..5) {

                val y = center.y + i * 18.dp.toPx()

                drawLine(
                    color = Color(0x228F32FF),
                    start = Offset(
                        center.x - radius * .85f,
                        y
                    ),
                    end = Offset(
                        center.x + radius * .85f,
                        y
                    ),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Holographic female figure
            drawZiraFigure(
                center = center,
                scale = radius / 120f
            )

            // Central energy core
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        Color.White,
                        Color(0xFFD68CFF),
                        Color(0x669C27FF),
                        Color.Transparent
                    )
                ),
                radius = 28.dp.toPx(),
                center = center
            )
        }
    }
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawZiraFigure(
    center: Offset,
    scale: Float
) {

    val glow = Color(0xFFD99AFF)

    // Head
    drawCircle(
        color = glow.copy(alpha = .85f),
        radius = 19f * scale,
        center = Offset(
            center.x,
            center.y - 58f * scale
        )
    )

    // Neck
    drawLine(
        color = glow.copy(alpha = .75f),
        start = Offset(
            center.x,
            center.y - 39f * scale
        ),
        end = Offset(
            center.x,
            center.y - 28f * scale
        ),
        strokeWidth = 7f * scale
    )

    // Torso
    val torsoPath = Path().apply {

        moveTo(
            center.x - 25f * scale,
            center.y - 28f * scale
        )

        quadraticTo(
            center.x - 34f * scale,
            center.y + 10f * scale,
            center.x - 18f * scale,
            center.y + 42f * scale
        )

        lineTo(
            center.x + 18f * scale,
            center.y + 42f * scale
        )

        quadraticTo(
            center.x + 34f * scale,
            center.y + 10f * scale,
            center.x + 25f * scale,
            center.y - 28f * scale
        )

        close()
    }

    drawPath(
        path = torsoPath,
        color = glow.copy(alpha = .52f)
    )

    // Left arm
    drawLine(
        color = glow.copy(alpha = .75f),
        start = Offset(
            center.x - 24f * scale,
            center.y - 22f * scale
        ),
        end = Offset(
            center.x - 50f * scale,
            center.y + 30f * scale
        ),
        strokeWidth = 8f * scale,
        cap = StrokeCap.Round
    )

    // Right arm
    drawLine(
        color = glow.copy(alpha = .75f),
        start = Offset(
            center.x + 24f * scale,
            center.y - 22f * scale
        ),
        end = Offset(
            center.x + 50f * scale,
            center.y + 30f * scale
        ),
        strokeWidth = 8f * scale,
        cap = StrokeCap.Round
    )

    // Left leg
    drawLine(
        color = glow.copy(alpha = .8f),
        start = Offset(
            center.x - 10f * scale,
            center.y + 40f * scale
        ),
        end = Offset(
            center.x - 24f * scale,
            center.y + 92f * scale
        ),
        strokeWidth = 9f * scale,
        cap = StrokeCap.Round
    )

    // Right leg
    drawLine(
        color = glow.copy(alpha = .8f),
        start = Offset(
            center.x + 10f * scale,
            center.y + 40f * scale
        ),
        end = Offset(
            center.x + 24f * scale,
            center.y + 92f * scale
        ),
        strokeWidth = 9f * scale,
        cap = StrokeCap.Round
    )

    // Holographic scan highlights
    for (i in 0..5) {

        val y = center.y - 75f * scale + i * 30f * scale

        drawLine(
            color = Color.White.copy(alpha = .18f),
            start = Offset(
                center.x - 45f * scale,
                y
            ),
            end = Offset(
                center.x + 45f * scale,
                y
            ),
            strokeWidth = 1.5f * scale
        )
    }
}

@Composable
fun ZiraButton(
    text: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(52.dp)
            .background(
                Color(0x221A0028),
                RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Color(0xFFD8A5FF),
            fontSize = 12.sp,
            letterSpacing = 2.sp
        )
    }
}
