package com.example.yandexcup2023.development.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.yandexcup2023.development.model.Sample

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SamplesMenu(
    expanded: Boolean,
    samples: List<Sample>,
    sampleIndexFocused: Sample?,
    onDismiss: () -> Unit,
    onSampleFocused: (Sample) -> Unit,
    onSampleClick: (Sample) -> Unit
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
        ),
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .widthIn(max = 60.dp)
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                ),
            offset = DpOffset(0.dp, (-18).dp)
        ) {
            samples.forEach { sample ->
                Row(
                    modifier = Modifier
                        .heightIn(min = 40.dp)
                        .fillMaxWidth()
                        .background(
                            if (sampleIndexFocused == sample)
                                Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    0.2f to MaterialTheme.colorScheme.primary,
                                    0.8f to MaterialTheme.colorScheme.primary,
                                    1f to Color.Transparent
                                )
                            else
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Transparent)
                                )
                        )
                        .combinedClickable(
                            onClick = { onSampleClick(sample) },
                            onLongClick = { onSampleFocused(sample) }
                        )
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(sample.name),
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}