package com.zira.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

enum class ZState { IDLE, LISTENING, THINKING, SPEAKING, SUCCESS, ERROR }

private val PURPLE = Color(0xFFB967FF)
private val PALE = Color(0xFFF0D7FF)
private val DARK = Color(0xFF09000F)

class MainActivity : ComponentActivity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContent { Zira() }
    }
}

@Composable
fun Zira() {
    var state by remember { mutableStateOf(ZState.IDLE) }

    val message = when (state) {
        ZState.IDLE -> "Online, sir. What do you need, Josh?"
        ZState.LISTENING -> "I'm listening, sir."
        ZState.THINKING -> "Thinking, Josh..."
        ZState.SPEAKING -> "Right away, sir."
        ZState.SUCCESS -> "Done, sir."
        ZState.ERROR -> "Something went wrong."
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF21002F), DARK, Color.Black)
                )
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            Text("ZIRA", color = PALE, fontSize = 32.sp)
            Text("ONLINE", color = PURPLE, fontSize = 11.sp, letterSpacing = 4.sp)

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                Alignment.Center
            ) {
                ZiraOrb(state)
            }

            Text(message, color = PALE, fontSize = 16.sp)

            Spacer(Modifier.height(18.dp))

            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceEvenly
            ) {
                ZButton("MIC") {
                    state = if (state == ZState.LISTENING)
                        ZState.IDLE else ZState.LISTENING
                }

                ZButton("THINK") {
                    state = ZState.THINKING
                }

                ZButton("DONE") {
                    state = ZState.SUCCESS
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                when (state) {
                    ZState.IDLE -> "READY • STANDING BY"
                    ZState.LISTENING -> "LISTENING"
                    ZState.THINKING -> "THINKING"
                    ZState.SPEAKING -> "SPEAKING"
                    ZState.SUCCESS -> "TASK COMPLETE"
                    ZState.ERROR -> "SYSTEM ERROR"
                },
                color = PURPLE,
                fontSize = 10.sp,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun ZButton(label: String, action: () -> Unit) {
    Box(
        Modifier
            .size(90.dp, 46.dp)
            .clip(CircleShape)
            .background(Color(0xFF180020))
            .clickable { action() },
        Alignment.Center
    ) {
        Text(label, color = PALE, fontSize = 11.sp)
    }
}

@Composable
fun ZiraOrb(state: ZState) {
    val a = rememberInfiniteTransition(label = "zira")

    val spin by a.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(7000, easing = LinearEasing)),
        label = "spin"
    )

    val pulse by a.animateFloat(
        0.92f, 1.08f,
        infiniteRepeatable(
            tween(
                when (state) {
                    ZState.LISTENING -> 450
                    ZState.THINKING -> 300
                    ZState.SUCCESS -> 250
                    else -> 1200
                }
            ),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val bob by a.animateFloat(
        -5f, 5f,
        infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "bob"
    )

    Canvas(Modifier.size(330.dp)) {
        val x = size.width / 2f
        val y = size.height / 2f
        val r = size.minDimension * .32f

        drawCircle(
            Brush.radialGradient(
                listOf(
                    PURPLE.copy(.45f),
                    PURPLE.copy(.12f),
                    Color.Transparent
                )
            ),
            r * 1.7f,
            Offset(x, y)
        )

        drawCircle(
            Brush.radialGradient(
                listOf(Color(0xFF7425B5), DARK, Color.Black)
            ),
            r,
            Offset(x, y)
        )

        drawCircle(
            PURPLE,
            r * pulse,
            Offset(x, y),
            style = Stroke(3f)
        )

        drawOval(
            PURPLE.copy(.65f),
            Offset(x - r * 1.2f, y - r * .4f),
            androidx.compose.ui.geometry.Size(r * 2.4f, r * .8f),
            style = Stroke(2f)
        )

        val ang = Math.toRadians(spin.toDouble())

        drawCircle(
            PALE,
            5f * pulse,
            Offset(
                x + cos(ang).toFloat() * r * 1.2f,
                y + sin(ang).toFloat() * r * .4f
            )
        )

        if (state == ZState.LISTENING) {
            drawCircle(
                PALE.copy(.5f),
                r * 1.25f * pulse,
                Offset(x, y),
                style = Stroke(2f)
            )
        }

        if (state == ZState.THINKING) {
            repeat(3) {
                val q = ang + it * 2.1
                drawCircle(
                    PALE,
                    4f,
                    Offset(
                        x + cos(q).toFloat() * r * 1.35f,
                        y + sin(q).toFloat() * r * 1.35f
                    )
                )
            }
        }

        // Zira's hologram
        val hx = x
        val hy = y + bob

        drawCircle(
            PALE,
            12f,
            Offset(hx, hy - 42f)
        )

        drawLine(
            PALE,
            Offset(hx - 10f, hy - 27f),
            Offset(hx - 28f, hy + 15f),
            3f
        )

        drawLine(
            PALE,
            Offset(hx + 10f, hy - 27f),
            Offset(hx + 28f, hy + 15f),
            3f
        )

        drawLine(
            PALE,
            Offset(hx - 8f, hy - 25f),
            Offset(hx - 10f, hy + 45f),
            3f
        )

        drawLine(
            PALE,
            Offset(hx + 8f, hy - 25f),
            Offset(hx + 10f, hy + 45f),
            3f
        )

        drawLine(
            PALE,
            Offset(hx - 10f, hy + 45f),
            Offset(hx - 18f, hy + 72f),
            3f
        )

        drawLine(
            PALE,
            Offset(hx + 10f, hy + 45f),
            Offset(hx + 18f, hy + 72f),
            3f
        )

        val eye = if (
            state == ZState.LISTENING
        ) 3.5f else 2.5f

        drawCircle(PALE, eye, Offset(hx - 5f, hy - 43f))
        drawCircle(PALE, eye, Offset(hx + 5f, hy - 43f))

        if (state == ZState.SPEAKING) {
            drawCircle(
                PALE,
                4f * pulse,
                Offset(hx, hy - 34f)
            )
        }
    }
}
