package com.example.yandexcup2023.development.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VisualArea(
    visualList: List<Pair<Int, Boolean>>
) {
    val activeColor = MaterialTheme.colorScheme.secondary
    Canvas(
        modifier = Modifier
            .heightIn(min = 30.dp)
            .fillMaxWidth()
    ) {
        val listSize = visualList.size
        visualList.forEachIndexed { index, i ->
            drawLine(
                if (i.second) activeColor else Color.White,
                Offset(size.width / listSize * index, (size.height - i.first.dp.toPx()) / 2),
                Offset(size.width / listSize * index, (size.height + i.first.dp.toPx()) / 2),
                1.dp.toPx()
            )
        }
    }
}