package com.example.yandexcup2023.development.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.yandexcup2023.R
import com.example.yandexcup2023.development.model.Layer

@Composable
fun LayersMenu(
    isLayerListOpen: Boolean,
    layers: List<Layer>,
    currentLayer: Layer?,
    closeLayerList: () -> Unit,
    deleteLayer: (Layer) -> Unit,
    selectLayer: (Layer) -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = Color.Transparent
        )
    ) {
        DropdownMenu(
            expanded = isLayerListOpen,
            onDismissRequest = closeLayerList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            offset = DpOffset(0.dp, 12.dp)
        ) {
            layers.forEach {

                val isActive by it.isActive.collectAsState()
                val isMuted by it.isMuted.collectAsState()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            color = if (currentLayer == it)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { selectLayer(it) }
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.name,
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        imageVector = if (isActive)
                            ImageVector.vectorResource(R.drawable.ic_pause)
                        else
                            ImageVector.vectorResource(R.drawable.ic_play),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(18.dp)
                            .clickable {
                                if (isActive) it.pause() else it.play()
                            }
                            .padding(vertical = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        imageVector = if (isMuted)
                            ImageVector.vectorResource(R.drawable.ic_muted)
                        else
                            ImageVector.vectorResource(R.drawable.ic_audible),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(18.dp)
                            .clickable {
                                if (isMuted) it.unMute() else it.mute()
                            }
                            .padding(vertical = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { deleteLayer(it) },
                        modifier = Modifier
                            .size(40.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}