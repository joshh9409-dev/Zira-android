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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

enum class ZState {
    IDLE, LISTENING, THINKING, SPEAKING, PERMISSION, SUCCESS, ERROR
}

private val P = Color(0xFFB967FF)
private val L = Color(0xFFF0D7FF)
private val B = Color(0xFF050008)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Zira() }
    }
}

@Composable
private fun Zira() {
    var state by remember { mutableStateOf(ZState.IDLE) }

    val text = when (state) {
        ZState.IDLE -> "Online, sir. What do you need, Josh?"
        ZState.LISTENING -> "I'm listening, sir."
        ZState.THINKING -> "Thinking, Josh..."
        ZState.SPEAKING -> "Right away, sir."
        ZState.PERMISSION -> "Permission required, Josh."
        ZState.SUCCESS -> "Done, sir."
        ZState.ERROR -> "Something went wrong."
    }

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                listOf(Color(0xFF190025), B, Color.Black)
            )
        )
    ) {
        Column(
            Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ZIRA", color = L, fontSize = 32.sp)
            Text("ONLINE", color = P, fontSize = 12.sp, letterSpacing = 4.sp)

            Box(
                Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Orb(state)
            }

            Text(text, color = L, fontSize = 16.sp)

            Spacer(Modifier.height(14.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button("MIC") {
                    state = if (state == ZState.LISTENING)
                        ZState.IDLE else ZState.LISTENING
                }

                Button("THINK") {
                    state = ZState.THINKING
                }

                Button("MENU") {
                    state = ZState.PERMISSION
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                when (state) {
                    ZState.IDLE -> "READY • STANDING BY"
                    ZState.LISTENING -> "LISTENING"
                    ZState.THINKING -> "THINKING"
                    ZState.SPEAKING -> "SPEAKING"
                    ZState.PERMISSION -> "AWAITING PERMISSION"
                    ZState.SUCCESS -> "TASK COMPLETE"
                    ZState.ERROR -> "SYSTEM ERROR"
                },
                color = P.copy(alpha = .8f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun Button(name: String, action: () -> Unit) {
    Box(
        Modifier
            .size(92.dp, 46.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF190027))
            .border(1.dp, P, RoundedCornerShape(24.dp))
            .clickable { action() },
        contentAlignment = Alignment.Center
    ) {
        Text(name, color = L, fontSize = 11.sp)
    }
}

@Composable
private fun Orb(state: ZState) {
    val a = rememberInfiniteTransition(label = "a")

    val spin by a.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "spin"
    )

    val pulse by a.animateFloat(
        .9f, 1.08f,
        infiniteRepeatable(tween(
            if (state == ZState.LISTENING) 500
            else if (state == ZState.THINKING) 350
            else if (state == ZState.SPEAKING) 250
            else 1400
        ), RepeatMode.Reverse),
        label = "pulse"
    )

    val float by a.animateFloat(
        -5f, 5f,
        infiniteRepeatable(tween(1600), RepeatMode.Reverse),
        label = "float"
    )

    Canvas(Modifier.size(350.dp)) {
        val x = size.width / 2
        val y = size.height / 2
        val r = size.minDimension * .34f

        drawCircle(
            brush = Brush.radialGradient(
                listOf(P.copy(alpha = .4f), P.copy(alpha = .08f), Color.Transparent)
            ),
            radius = r * 1.6f,
            center = Offset(x, y)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF7020B5), Color(0xFF160020), B)
            ),
            radius = r,
            center = Offset(x, y)
        )

        drawCircle(
            P,
            r * pulse,
            Offset(x, y),
            style = Stroke(3f)
        )

        drawOval(
            P.copy(alpha = .65f),
            Offset(x - r * 1.2f, y - r * .4f),
            Size(r * 2.4f, r * .8f),
            style = Stroke(2f)
        )

        val ang = Math.toRadians(spin.toDouble())

        drawCircle(
            L,
            5f * pulse,
            Offset(
                x + cos(ang).toFloat() * r * 1.2f,
                y + sin(ang).toFloat() * r * .4f
            )
        )

        if (state == ZState.LISTENING) {
            drawCircle(
                L.copy(alpha = .45f),
                r * 1.25f * pulse,
                Offset(x, y),
                style = Stroke(2f)
            )
        }

        if (state == ZState.THINKING) {
            for (i in 0..2) {
                val q = ang + i * 2.1
                drawCircle(
                    L,
                    4f,
                    Offset(
                        x + cos(q).toFloat() * r * 1.35f,
                        y + sin(q).toFloat() * r * 1.35f
                    )
                )
            }
        }

        Avatar(
            Offset(x, y + float),
            r / 100f,
            state,
            pulse
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.Avatar(
    c: Offset,
    s: Float,
    state: ZState,
    pulse: Float
) {
    val head = Offset(c.x, c.y - 45f * s)

    drawCircle(
        P.copy(alpha = .22f),
        24f * s * pulse,
        head
    )

    drawCircle(
        L.copy(alpha = .9f),
        12f * s,
        head,
        style = Stroke(2.5f * s)
    )

    drawLine(
        L.copy(alpha = .8f),
        Offset(c.x - 12f * s, c.y - 25f * s),
        Offset(c.x - 32f * s, c.y + 15f * s),
        3f * s
    )

    drawLine(
        L.copy(alpha = .8f),
        Offset(c.x + 12f * s, c.y - 25f * s),
        Offset(c.x + 32f * s, c.y + 15f * s),
        3f * s
    )

    drawLine(
        L.copy(alpha = .85f),
        Offset(c.x - 9f * s, c.y - 25f * s),
        Offset(c.x - 11f * s, c.y + 50f * s),
        3f * s
    )

    drawLine(
        L.copy(alpha = .85f),
        Offset(c.x + 9f * s, c.y - 25f * s),
        Offset(c.x + 11f * s, c.y + 50f * s),
        3f * s
    )

    drawLine(
        L.copy(alpha = .85f),
        Offset(c.x - 11f * s, c.y + 50f * s),
        Offset(c.x - 18f * s, c.y + 80f * s),
        3f * s
    )

    drawLine(
        L.copy(alpha = .85f),
        Offset(c.x + 11f * s, c.y + 50f * s),
        Offset(c.x + 18f * s, c.y + 80f * s),
        3f * s
    )

    val eye = if (
        state == ZState.LISTENING ||
        state == ZState.SPEAKING
    ) 3f * pulse else 2.5f

    drawCircle(L, eye * s, Offset(head.x - 5f * s, head.y))
    drawCircle(L, eye * s, Offset(head.x + 5f * s, head.y))

    if (state == ZState.SPEAKING) {
        drawCircle(
            L,
            4f * s * pulse,
            Offset(head.x, head.y + 8f * s)
        )
    }
}
