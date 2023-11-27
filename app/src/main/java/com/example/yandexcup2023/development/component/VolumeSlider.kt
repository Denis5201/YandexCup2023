package com.example.yandexcup2023.development.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.yandexcup2023.R
import com.example.yandexcup2023.development.VOLUME_STEP_COUNT
import com.example.yandexcup2023.development.model.Layer
import kotlin.math.roundToInt

@Composable
fun VolumeSlider(
    volumeOffset: Float,
    density: Density,
    currentLayer: Layer?,
    setMaxVolume: (Float) -> Unit,
    setOffset: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxHeight(),
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.ic_volume),
            contentDescription = stringResource(R.string.volume),
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 16.dp),
            contentScale = ContentScale.FillHeight
        )
        val maxVolumeOffset = density.run { (minHeight - 50.dp).toPx() }
        LaunchedEffect(key1 = Unit) {
            setMaxVolume(maxVolumeOffset / VOLUME_STEP_COUNT)
        }
        LaunchedEffect(key1 = currentLayer) {
            currentLayer?.currentVolume?.let {
                setOffset(-maxVolumeOffset * it)
            }
        }
        Text(
            text = stringResource(R.string.volume),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .offset {
                    IntOffset(
                        -28.dp.roundToPx(),
                        volumeOffset.roundToInt()
                    )
                }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState {
                        setOffset((volumeOffset + it).coerceIn(-maxVolumeOffset, 0f))
                    }
                )
                .rotate(-90f)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 3.dp)
                .align(Alignment.BottomStart),
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}