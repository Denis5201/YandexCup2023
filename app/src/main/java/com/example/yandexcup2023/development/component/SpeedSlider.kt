package com.example.yandexcup2023.development.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.yandexcup2023.R
import com.example.yandexcup2023.development.SPEED_STEP_COUNT
import com.example.yandexcup2023.development.model.InstrumentalLayer
import com.example.yandexcup2023.development.model.Layer
import kotlin.math.roundToInt

@Composable
fun SpeedSlider(
    speedOffset: Float,
    density: Density,
    currentLayer: Layer?,
    setMaxSpeed: (Float) -> Unit,
    setOffset: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.ic_speed),
            contentDescription = stringResource(R.string.speed),
            modifier = Modifier
                .padding(start = 32.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        val maxSpeedOffset = density.run { (minWidth - 67.dp).toPx() }
        LaunchedEffect(key1 = Unit) {
            setMaxSpeed(maxSpeedOffset / SPEED_STEP_COUNT)
        }
        LaunchedEffect(key1 = currentLayer) {
            currentLayer?.let {
                if (it is InstrumentalLayer) {
                    setOffset(it.currentSpeedBoost * maxSpeedOffset / it.sample.defaultDelay)
                }
            }
        }
        Text(
            text = stringResource(R.string.speed),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset { IntOffset(speedOffset.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState {
                        setOffset((speedOffset + it).coerceIn(0f, maxSpeedOffset))
                    }
                )
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 6.dp),
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}