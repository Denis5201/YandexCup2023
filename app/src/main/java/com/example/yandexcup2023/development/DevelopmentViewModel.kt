package com.example.yandexcup2023.development

import android.content.Context
import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandexcup2023.R
import com.example.yandexcup2023.development.model.DevelopmentScreenState
import com.example.yandexcup2023.development.model.Instrument
import com.example.yandexcup2023.development.model.InstrumentalLayer
import com.example.yandexcup2023.development.model.Layer
import com.example.yandexcup2023.development.model.MicrophoneLayer
import com.example.yandexcup2023.development.model.Sample
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DevelopmentViewModel : ViewModel() {

    private val _state = MutableStateFlow(DevelopmentScreenState())
    val state: StateFlow<DevelopmentScreenState> = _state.asStateFlow()

    private val availableSamples = listOf(
        Sample(R.raw.gitara_akkord, R.string.sample_guitar_high, Instrument.Guitar),
        Sample(R.raw.strunyi, R.string.sample_guitar_low, Instrument.Guitar),
        Sample(R.raw.zvuk_malogo_barabana, R.string.sample_snare, Instrument.Percussion),
        Sample(R.raw.sner_priglushennyiy, R.string.sample_snare_muffled, Instrument.Percussion),
        Sample(R.raw.clarinet_note, R.string.sample_clarinet, Instrument.Brass),
        Sample(R.raw.saksofon_odinochnyiy, R.string.sample_saxophone, Instrument.Brass)
    )

    private var maxVolumeValue = 10f
    private var maxSpeedValue = 10f

    private var recorder = MediaRecorder()

    private val _visualList = MutableStateFlow(List(VISUAL_LINE_LENGTH) { Pair((10..30).random(), false) })
    var visualList = _visualList.asStateFlow()


    fun setMaxVolume(value: Float) {
        maxVolumeValue = value
    }

    fun setMaxSpeed(value: Float) {
        maxSpeedValue = value
    }

    fun onInstrumentSelect(instrument: Instrument) {
        _state.value = _state.value.copy(
            instrumentSelected = instrument,
            samples = availableSamples.filter { it.type == instrument }
        )
    }

    fun onDismissInstrumentSelected() {
        _state.value = _state.value.copy(
            instrumentSelected = null,
            sampleFocused = null
        )
    }

    fun addSample(context: Context, sample: Sample) {
        val newLayer = InstrumentalLayer(
            context, sample, maxVolumeValue, maxSpeedValue,
            "${context.resources.getString(sample.name)} ${_state.value.layerList
                .filterIsInstance<InstrumentalLayer>()
                .filter { it.sample == sample }.size + 1}"
        )
        _state.value = _state.value.copy(
            layerList = _state.value.layerList.plus(newLayer),
            currentLayer = newLayer,
            instrumentSelected = null
        )
    }

    fun addDefaultSample(context: Context, instrument: Instrument) {
        _state.value = _state.value.copy(
            instrumentSelected = instrument
        )
        val defaultSample = availableSamples.first { it.type == instrument }
        val newLayer = InstrumentalLayer(
            context, defaultSample, maxVolumeValue, maxSpeedValue,
            "${context.resources.getString(defaultSample.name)} ${_state.value.layerList
                .filterIsInstance<InstrumentalLayer>()
                .filter { it.sample == defaultSample }.size + 1}"
        )
        _state.value = _state.value.copy(
            layerList = _state.value.layerList.plus(newLayer),
            currentLayer = newLayer,
            instrumentSelected = null
        )
    }

    fun onSampleFocused(sample: Sample) {
        _state.value = _state.value.copy(sampleFocused = sample)
    }

    fun onLayerListOpen() {
        _state.value = _state.value.copy(
            isLayerListOpen = true
        )
    }

    fun onDismissLayerList() {
        _state.value = _state.value.copy(
            isLayerListOpen = false
        )
    }

    fun selectLayer(layer: Layer) {
        _state.value = _state.value.copy(
            currentLayer = layer,
            isLayerListOpen = false
        )
    }

    fun deleteLayer(layer: Layer) {
        layer.delete()
        _state.value = _state.value.copy(
            currentLayer = if (_state.value.currentLayer == layer) null else _state.value.currentLayer,
            layerList = _state.value.layerList.minus(layer)
        )
    }

    fun startMicrophoneRecording(path: String) {
        recorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(path)
            prepare()
            start()
        }
        _state.value = _state.value.copy(
            isMicrophoneRecording = true,
            currentRecorderFileRoute = path
        )
        getVisual()
    }

    fun stopMicrophoneRecording(context: Context) {
        if (state.value.isMicrophoneRecording) {
            recorder.stop()
            val newLayer = MicrophoneLayer(
                maxVolumeValue, maxSpeedValue,
                context.resources.getString(
                    R.string.layer_microphone, _state.value.layerList
                        .filterIsInstance<MicrophoneLayer>().size + 1
                ),
                state.value.currentRecorderFileRoute
                )
            _state.value = _state.value.copy(
                layerList = _state.value.layerList.plus(newLayer),
                currentLayer = newLayer,
                isMicrophoneRecording = false,
                currentRecorderFileRoute = ""
            )
        }
    }

    fun startTrackRecording(path: String) {
        recorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(path)
            prepare()
            start()
        }
        state.value.layerList.forEach {
            if (it.isMuted.value.not()) it.play()
        }
        _state.value = _state.value.copy(
            isAllTrackRecording = true,
            currentRecorderFileRoute = path
        )
        getVisual()
    }

    fun stopTrackRecording() {
        recorder.stop()
        state.value.layerList.forEach {
            it.pause()
        }
        _state.value = _state.value.copy(
            isAllTrackRecording = false,
            currentRecorderFileRoute = ""
        )
    }

    fun playAllTrack() {
        state.value.layerList.forEach {
            if (it.isMuted.value.not()) it.play()
        }
        _state.value = _state.value.copy(
            isAllTrackPlaying = true
        )
    }

    fun pauseAllTrack() {
        state.value.layerList.forEach {
            it.pause()
        }
        _state.value = _state.value.copy(
            isAllTrackPlaying = false
        )
    }

    private fun getVisual() {
        viewModelScope.launch {
            while (state.value.isMicrophoneRecording || state.value.isAllTrackRecording) {
                val percent = recorder.maxAmplitude.toFloat() / AMPLITUDE_DIVIDER
                _visualList.value = _visualList.value.mapIndexed { index, pair ->
                    if (index < percent)
                        pair.copy(second = true)
                    else
                        pair.copy(second = false)
                }
                delay(VISUAL_UPDATE_DELAY)
            }
            _visualList.value = _visualList.value.map { it.copy(second = false) }
        }
    }

    companion object {
        const val VISUAL_LINE_LENGTH = 80
        const val AMPLITUDE_DIVIDER = 500
        const val VISUAL_UPDATE_DELAY = 400L
    }
}