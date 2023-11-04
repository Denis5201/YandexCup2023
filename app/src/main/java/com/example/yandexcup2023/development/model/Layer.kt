package com.example.yandexcup2023.development.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class Layer(
    val maxVolumeValue: Float,
    val maxSpeedValue: Float,
    val name: String
) {
    protected val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()
    protected val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    var currentVolume = 0.5f
        protected set
    var currentSpeedBoost = 0L
        protected set

    open fun play() {
        if (!isActive.value) {
            _isActive.value = true
            repeat()
        }
    }

    open fun pause() {
        _isActive.value = false
    }

    abstract fun setVolume(value: Float)

    abstract fun setSpeedBoost(value: Float)

    abstract fun mute()

    abstract fun unMute()

    abstract fun delete()

    protected abstract fun repeat()
}