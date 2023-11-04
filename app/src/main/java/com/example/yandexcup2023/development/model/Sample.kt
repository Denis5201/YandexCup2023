package com.example.yandexcup2023.development.model

data class Sample(
    val resId: Int,
    val name: Int,
    val type: Instrument,
    val defaultDelay: Long = DEFAULT_DELAY
) {
    private companion object {
        const val DEFAULT_DELAY = 2000L
    }
}
