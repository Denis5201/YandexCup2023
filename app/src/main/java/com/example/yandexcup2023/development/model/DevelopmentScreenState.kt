package com.example.yandexcup2023.development.model

data class DevelopmentScreenState(
    val layerList: List<Layer> = emptyList(),
    val currentLayer: Layer? = null,
    val instrumentSelected: Instrument? = null,
    val samples: List<Sample> = emptyList(),
    val sampleFocused: Sample? = null,
    val isLayerListOpen: Boolean = false,
    val isMicrophoneRecording: Boolean = false,
    val isAllTrackRecording: Boolean = false,
    val isAllTrackPlaying: Boolean = false,
    val currentRecorderFileRoute: String = ""
)