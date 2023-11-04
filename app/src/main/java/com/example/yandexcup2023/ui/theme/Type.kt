package com.example.yandexcup2023.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.yandexcup2023.R

val YandexText = FontFamily(
    Font(R.font.yandex_text_regular, FontWeight.W400)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = YandexText,
        fontWeight = FontWeight.W400,
        fontSize = 13.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = YandexText,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )
)