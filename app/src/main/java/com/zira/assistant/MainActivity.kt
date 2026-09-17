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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

private val DeepSpace = Color(0xFF07000F)
private val Purple = Color(0xFFB967FF)
private val BrightPurple = Color(0xFFF0D7FF)
private val SoftPurple = Color(0xFF8D35E8)

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
            animation = tween(
                durationMillis = 9000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulse by transition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF160027),
                        DeepSpace,
                        Color(0xFF020006)
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
                color = BrightPurple,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "ONLINE",
                color = Purple,
                fontSize = 12.sp,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .size(350.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val radius = size.minDimension * 0.37f

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                BrightPurple.copy(alpha = 0.45f),
                                Purple.copy(alpha = 0.18f),
                                SoftPurple.copy(alpha = 0.06f),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy),
                            radius = radius * 1.55f
                        ),
                        radius = radius * 1.5f,
                        center = Offset(cx, cy)
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF6A16B8),
                                Color(0xFF260044),
                                Color(0xFF090014)
                            ),
                            center = Offset(cx - radius * 0.25f, cy - radius * 0.3f),
                            radius = radius
                        ),
                        radius = radius,
                        center = Offset(cx, cy)
                    )

                    drawCircle(
                        color = Purple.copy(alpha = 0.8f),
                        radius = radius,
                        center = Offset(cx, cy),
                        style = Stroke(width = 3f)
                    )

                    drawCircle(
                        color = BrightPurple.copy(alpha = 0.35f),
                        radius = radius * 0.88f,
                        center = Offset(cx, cy),
                        style = Stroke(width = 1.5f)
                    )

                    val angle = Math.toRadians(rotation.toDouble())

                    val orbitX = cx + cos(angle).toFloat() * radius * 1.15f
                    val orbitY = cy + sin(angle).toFloat() * radius * 0.42f

                    drawCircle(
                        color = BrightPurple,
                        radius = 5f,
                        center = Offset(orbitX, orbitY)
                    )

                    drawOval(
                        color = Purple.copy(alpha = 0.65f),
                        topLeft = Offset(
                            cx - radius * 1.15f,
                            cy - radius * 0.42f
                        ),
                        size = androidx.compose.ui.geometry.Size(
                            radius * 2.3f,
                            radius * 0.84f
                        ),
                        style = Stroke(width = 2f)
                    )

                    drawOval(
                        color = SoftPurple.copy(alpha = 0.5f),
                        topLeft = Offset(
                            cx - radius * 1.28f,
                            cy - radius * 0.65f
                        ),
                        size = androidx.compose.ui.geometry.Size(
                            radius * 2.56f,
                            radius * 1.3f
                        ),
                        style = Stroke(width = 1.5f)
                    )

                    drawLine(
                        color = BrightPurple.copy(alpha = 0.25f),
                        start = Offset(cx - radius, cy),
                        end = Offset(cx + radius, cy),
                        strokeWidth = 1f
                    )

                    drawLine(
                        color = BrightPurple.copy(alpha = 0.18f),
                        start = Offset(cx, cy - radius),
                        end = Offset(cx, cy + radius),
                        strokeWidth = 1f
                    )

                    drawZiraFigure(
                        center = Offset(cx, cy + radius * 0.02f),
                        scale = radius / 100f,
                        glow = pulse
                    )
                }
            }

            Text(
                text = "Online, sir. What do you need, Josh?",
                color = BrightPurple,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ZiraButton("MIC")
                ZiraButton("CHAT")
                ZiraButton("MENU")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "READY • LISTENING FOR COMMAND",
                color = Purple.copy(alpha = 0.75f),
                fontSize = 10.sp,
                letterSpacing = 1.5.sp
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawZiraFigure(
    center: Offset,
    scale: Float,
    glow: Float
) {

    val c = BrightPurple.copy(alpha = 0.72f * glow.coerceAtMost(1f))

    val head = Offset(
        center.x,
        center.y - 48f * scale
    )

    drawCircle(
        color = c.copy(alpha = 0.16f),
        radius = 25f * scale,
        center = head
    )

    drawCircle(
        color = c,
        radius = 12f * scale,
        center = head,
        style = Stroke(width = 2f)
    )

    val hair = Path().apply {
        moveTo(
            head.x - 14f * scale,
            head.y - 8f * scale
        )
        quadraticTo(
            head.x - 27f * scale,
            head.y + 10f * scale,
            head.x - 17f * scale,
            head.y + 22f * scale
        )
        quadraticTo(
            head.x,
            head.y + 9f * scale,
            head.x + 17f * scale,
            head.y + 22f * scale
        )
        quadraticTo(
            head.x + 27f * scale,
            head.y + 10f * scale,
            head.x + 14f * scale,
            head.y - 8f * scale
        )
    }

    drawPath(
        path = hair,
        color = Purple.copy(alpha = 0.7f),
        style = Stroke(width = 3f * scale)
    )

    val body = Path().apply {

        moveTo(
            center.x - 10f * scale,
            center.y - 34f * scale
        )

        cubicTo(
            center.x - 19f * scale,
            center.y - 25f * scale,
            center.x - 23f * scale,
            center.y - 5f * scale,
            center.x - 16f * scale,
            center.y + 10f * scale
        )

        cubicTo(
            center.x - 13f * scale,
            center.y + 25f * scale,
            center.x - 10f * scale,
            center.y + 40f * scale,
            center.x - 13f * scale,
            center.y + 58f * scale
        )

        moveTo(
            center.x + 10f * scale,
            center.y - 34f * scale
        )

        cubicTo(
            center.x + 19f * scale,
            center.y - 25f * scale,
            center.x + 23f * scale,
            center.y - 5f * scale,
            center.x + 16f * scale,
            center.y + 10f * scale
        )

        cubicTo(
            center.x + 13f * scale,
            center.y + 25f * scale,
            center.x + 10f * scale,
            center.y + 40f * scale,
            center.x + 13f * scale,
            center.y + 58f * scale
        )
    }

    drawPath(
        path = body,
        color = c,
        style = Stroke(
            width = 3f * scale,
            cap = StrokeCap.Round
        )
    )

    drawLine(
        color = c,
        start = Offset(
            center.x - 13f * scale,
            center.y - 25f * scale
        ),
        end = Offset(
            center.x - 35f * scale,
            center.y + 12f * scale
        ),
        strokeWidth = 3f * scale,
        cap = StrokeCap.Round
    )

    drawLine(
        color = c,
        start = Offset(
            center.x + 13f * scale,
            center.y - 25f * scale
        ),
        end = Offset(
            center.x + 35f * scale,
            center.y + 12f * scale
        ),
        strokeWidth = 3f * scale,
        cap = StrokeCap.Round
    )

    drawLine(
        color = c,
        start = Offset(
            center.x - 5f * scale,
            center.y + 54f * scale
        ),
        end = Offset(
            center.x - 18f * scale,
            center.y + 82f * scale
        ),
        strokeWidth = 3f * scale,
        cap = StrokeCap.Round
    )

    drawLine(
        color = c,
        start = Offset(
            center.x + 5f * scale,
            center.y + 54f * scale
        ),
        end = Offset(
            center.x + 18f * scale,
            center.y + 82f * scale
        ),
        strokeWidth = 3f * scale,
        cap = StrokeCap.Round
    )

    drawCircle(
        color = BrightPurple,
        radius = 2.5f * scale,
        center = Offset(
            center.x - 5f * scale,
            head.y
        )
    )

    drawCircle(
        color = BrightPurple,
        radius = 2.5f * scale,
        center = Offset(
            center.x + 5f * scale,
            head.y
        )
    )
}

@Composable
private fun ZiraButton(text: String) {

    Box(
        modifier = Modifier
            .size(width = 92.dp, height = 46.dp)
            .clip(RoundedCornerShape(23.dp))
            .background(Color(0xFF190027))
            .border(
                width = 1.dp,
                color = Purple.copy(alpha = 0.75f),
                shape = RoundedCornerShape(23.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = BrightPurple,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}
