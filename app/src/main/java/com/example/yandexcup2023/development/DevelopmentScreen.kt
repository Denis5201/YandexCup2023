package com.example.yandexcup2023.development

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yandexcup2023.development.component.BottomBar
import com.example.yandexcup2023.development.component.SpeedSlider
import com.example.yandexcup2023.development.component.ToolBar
import com.example.yandexcup2023.development.component.VisualArea
import com.example.yandexcup2023.development.component.VolumeSlider

const val VOLUME_STEP_COUNT = 50
const val SPEED_STEP_COUNT = 40

@Composable
fun DevelopmentScreen(
    viewModel: DevelopmentViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val density = LocalDensity.current
    val context = LocalContext.current
    var player: MediaPlayer? = null

    DisposableEffect(key1 = Unit) {
        onDispose {
            player?.release()
            player = null
        }
    }
    LaunchedEffect(state.sampleFocused) {
        state.sampleFocused?.let {
            player?.release()
            player = MediaPlayer.create(context, it.resId)
            player?.setVolume(0.5f, 0.5f)
            player?.start()
        }
    }

    var volumeOffset by remember { mutableStateOf(0f) }
    val sampleVolume by remember {
        derivedStateOf { -volumeOffset / VOLUME_STEP_COUNT }
    }
    var speedOffset by remember { mutableStateOf(0f) }
    val sampleSpeed by remember {
        derivedStateOf { speedOffset / SPEED_STEP_COUNT }
    }
    LaunchedEffect(key1 = sampleVolume) {
        state.currentLayer?.setVolume(sampleVolume)
    }
    LaunchedEffect(key1 = sampleSpeed) {
        state.currentLayer?.setSpeedBoost(sampleSpeed)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        ToolBar(
            instrumentSelected = state.instrumentSelected,
            samples = state.samples,
            sampleIndexFocused = state.sampleFocused,
            onSelect = { viewModel.onInstrumentSelect(it) },
            onDismissSelected = { viewModel.onDismissInstrumentSelected() },
            onSampleFocused = { viewModel.onSampleFocused(it) },
            onSampleClick = { viewModel.addSample(context, it) },
            onInstrumentClick = { viewModel.addDefaultSample(context, it) }
        )
        Spacer(modifier = Modifier.padding(38.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                        1f to MaterialTheme.colorScheme.surface
                    )
                )
        ) {
            VolumeSlider(
                volumeOffset = volumeOffset,
                density = density,
                currentLayer = state.currentLayer,
                setMaxVolume = { viewModel.setMaxVolume(it) },
                setOffset = { volumeOffset = it },
                modifier = Modifier
                    .padding(bottom = 36.dp)
                    .align(Alignment.CenterStart)
            )
            SpeedSlider(
                speedOffset = speedOffset,
                density = density,
                currentLayer = state.currentLayer,
                setMaxSpeed = { viewModel.setMaxSpeed(it) },
                setOffset = { speedOffset = it },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .heightIn(max = 16.dp)
                    .align(Alignment.BottomCenter)
            )
        }
        Spacer(modifier = Modifier.padding(12.dp))

        val visualList by viewModel.visualList.collectAsState()
        VisualArea(visualList = visualList)
        Spacer(modifier = Modifier.padding(10.dp))

        BottomBar(
            isLayerListOpen = state.isLayerListOpen,
            layers = state.layerList,
            currentLayer = state.currentLayer,
            isMicrophoneRecording = state.isMicrophoneRecording,
            isAllTrackRecording = state.isAllTrackRecording,
            isAllTrackPlaying = state.isAllTrackPlaying,
            openLayerList = { viewModel.onLayerListOpen() },
            closeLayerList = { viewModel.onDismissLayerList() },
            deleteLayer = { viewModel.deleteLayer(it) },
            selectLayer = { viewModel.selectLayer(it) },
            startMicrophoneRecording = { viewModel.startMicrophoneRecording(it) },
            stopMicrophoneRecording = { viewModel.stopMicrophoneRecording(context) },
            startAllTrackRecording = { viewModel.startTrackRecording(it) },
            stopAllTrackRecording = { viewModel.stopTrackRecording() },
            startAllTrackPlaying = { viewModel.playAllTrack() },
            stopAllTrackPlaying = { viewModel.pauseAllTrack() },
        )
    }
}