package com.example.yandexcup2023.development.model

import android.media.MediaPlayer
import java.io.File

class MicrophoneLayer(
    maxVolumeValue: Float,
    maxSpeedValue: Float,
    name: String,
    private val filePath: String
) : Layer(maxVolumeValue, maxSpeedValue, name) {

    private val player = MediaPlayer()

    init {
        player.setDataSource(filePath)
        player.prepare()
        player.setOnCompletionListener {
            _isActive.value = false
        }
        play()
    }

    override fun play() {
        if (_isActive.value) {
            player.pause()
            player.seekTo(0)
            player.start()
        } else {
            player.start()
        }
        _isActive.value = true
    }

    override fun pause() {
        super.pause()
        player.pause()
    }

    override fun setVolume(value: Float) {
        val currentVolume = (value / maxVolumeValue)
            .coerceAtLeast(0.05f)
        this.currentVolume = currentVolume
        if (!isMuted.value) {
            player.setVolume(currentVolume, currentVolume)
        }
    }

    override fun setSpeedBoost(value: Float) { }

    override fun mute() {
        _isMuted.value = true
        player.setVolume(0f, 0f)
    }

    override fun unMute() {
        _isMuted.value = false
        player.seekTo(0)
        player.setVolume(currentVolume, currentVolume)
    }

    override fun delete() {
        _isActive.value = false
        player.release()
        File(filePath).delete()
    }

    override fun repeat() {
    }
}