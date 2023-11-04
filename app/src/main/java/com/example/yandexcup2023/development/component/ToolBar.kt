package com.example.yandexcup2023.development.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.yandexcup2023.R
import com.example.yandexcup2023.development.model.Instrument
import com.example.yandexcup2023.development.model.Sample

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToolBar(
    instrumentSelected: Instrument?,
    samples: List<Sample>,
    sampleIndexFocused: Sample?,
    onSelect: (Instrument) -> Unit,
    onDismissSelected: () -> Unit,
    onSampleFocused: (Sample) -> Unit,
    onSampleClick: (Sample) -> Unit,
    onInstrumentClick: (Instrument) -> Unit
) {
    val selectedShape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50)
    val selectedColor = MaterialTheme.colorScheme.secondary
    val nothingSelectedColor = MaterialTheme.colorScheme.primary
    val otherSelectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = if (instrumentSelected == Instrument.Guitar)
                            selectedColor
                        else if (instrumentSelected != null)
                            otherSelectedColor
                        else
                            nothingSelectedColor,
                        shape = if (instrumentSelected == Instrument.Guitar) selectedShape else CircleShape
                    )
                    .combinedClickable(
                        onClick = { onInstrumentClick(Instrument.Guitar) },
                        onLongClick = { onSelect(Instrument.Guitar) }
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_guitar),
                    contentDescription = stringResource(R.string.guitar)
                )
            }
            Text(
                text = stringResource(R.string.guitar),
                color = if (instrumentSelected != null) otherSelectedColor else nothingSelectedColor,
                style = MaterialTheme.typography.bodyLarge
            )
            SamplesMenu(
                expanded = instrumentSelected == Instrument.Guitar,
                samples = samples,
                sampleIndexFocused = sampleIndexFocused,
                onDismiss = onDismissSelected,
                onSampleFocused = onSampleFocused,
                onSampleClick = onSampleClick
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = if (instrumentSelected == Instrument.Percussion)
                            selectedColor
                        else if (instrumentSelected != null)
                            otherSelectedColor
                        else
                            nothingSelectedColor,
                        shape = if (instrumentSelected == Instrument.Percussion) selectedShape else CircleShape
                    )
                    .combinedClickable(
                        onClick = { onInstrumentClick(Instrument.Percussion) },
                        onLongClick = { onSelect(Instrument.Percussion) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_percussion),
                    contentDescription = stringResource(R.string.percussion)
                )
            }
            Text(
                text = stringResource(R.string.percussion),
                color = if (instrumentSelected != null) otherSelectedColor else nothingSelectedColor,
                style = MaterialTheme.typography.bodyLarge
            )
            SamplesMenu(
                expanded = instrumentSelected == Instrument.Percussion,
                samples = samples,
                sampleIndexFocused = sampleIndexFocused,
                onDismiss = onDismissSelected,
                onSampleFocused = onSampleFocused,
                onSampleClick = onSampleClick
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = if (instrumentSelected == Instrument.Brass)
                            selectedColor
                        else if (instrumentSelected != null)
                            otherSelectedColor
                        else
                            nothingSelectedColor,
                        shape = if (instrumentSelected == Instrument.Brass) selectedShape else CircleShape
                    )
                    .combinedClickable(
                        onClick = { onInstrumentClick(Instrument.Brass) },
                        onLongClick = { onSelect(Instrument.Brass) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_brass),
                    contentDescription = stringResource(R.string.brass)
                )
            }
            Text(
                text = stringResource(R.string.brass),
                color = if (instrumentSelected != null) otherSelectedColor else nothingSelectedColor,
                style = MaterialTheme.typography.bodyLarge
            )
            SamplesMenu(
                expanded = instrumentSelected == Instrument.Brass,
                samples = samples,
                sampleIndexFocused = sampleIndexFocused,
                onDismiss = onDismissSelected,
                onSampleFocused = onSampleFocused,
                onSampleClick = onSampleClick
            )
        }
    }
}