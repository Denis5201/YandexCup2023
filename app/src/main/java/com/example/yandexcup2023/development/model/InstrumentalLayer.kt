package com.example.yandexcup2023.development.model

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InstrumentalLayer(
    context: Context,
    val sample: Sample,
    maxVolumeValue: Float,
    maxSpeedValue: Float,
    name: String
) : Layer(maxVolumeValue, maxSpeedValue, name) {

    private val player = MediaPlayer.create(context, sample.resId)

    init {
        player.setVolume(0.5f, 0.5f)
        play()
    }

    override fun setVolume(value: Float) {
        val currentVolume = (value / maxVolumeValue)
            .coerceAtLeast(0.05f)
        this.currentVolume = currentVolume
        if (!isMuted.value) {
            player.setVolume(currentVolume, currentVolume)
        }
    }

    override fun setSpeedBoost(value: Float) {
        val currentSpeed = (sample.defaultDelay * value / maxSpeedValue).coerceIn(0f, sample.defaultDelay.toFloat())
        this.currentSpeedBoost = currentSpeed.toLong()
    }

    override fun mute() {
        _isMuted.value = true
        player.setVolume(0f, 0f)
    }

    override fun unMute() {
        _isMuted.value = false
        player.setVolume(currentVolume, currentVolume)
    }

    override fun delete() {
        _isActive.value = false
        player.release()
    }

    override fun repeat() {
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive.value) {
                player.seekTo(0)
                player.start()
                delay(sample.defaultDelay - currentSpeedBoost)
            }
        }
    }
}