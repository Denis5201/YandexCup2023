package com.example.yandexcup2023.development.component

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.yandexcup2023.R
import com.example.yandexcup2023.development.model.Layer
import com.example.yandexcup2023.development.model.MicrophoneLayer
import java.io.File

const val BOTTOM_BAR_SIZE = 34
const val RECORD_TRACK_FILE_NAME = "record.mp3"

@Composable
fun BottomBar(
    isLayerListOpen: Boolean,
    layers: List<Layer>,
    currentLayer: Layer?,
    isMicrophoneRecording: Boolean,
    isAllTrackRecording: Boolean,
    isAllTrackPlaying: Boolean,
    openLayerList: () -> Unit,
    closeLayerList: () -> Unit,
    deleteLayer: (Layer) -> Unit,
    selectLayer: (Layer) -> Unit,
    startMicrophoneRecording: (String) -> Unit,
    stopMicrophoneRecording: () -> Unit,
    startAllTrackRecording: (String) -> Unit,
    stopAllTrackRecording: () -> Unit,
    startAllTrackPlaying: () -> Unit,
    stopAllTrackPlaying: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = BOTTOM_BAR_SIZE.dp)
                .background(
                    color = if (isLayerListOpen) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { openLayerList() }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.layers),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                imageVector = if (isLayerListOpen)
                    ImageVector.vectorResource(R.drawable.ic_close_menu)
                else
                    ImageVector.vectorResource(R.drawable.ic_show_menu),
                contentDescription = null
            )
            LayersMenu(
                isLayerListOpen = isLayerListOpen,
                layers = layers,
                currentLayer = currentLayer,
                closeLayerList = closeLayerList,
                deleteLayer = deleteLayer,
                selectLayer = selectLayer
            )
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { }
        val context = LocalContext.current

        Row(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .size(BOTTOM_BAR_SIZE.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        if (!isMicrophoneRecording) {
                            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.RECORD_AUDIO
                                )
                            ) {
                                val number = layers.filterIsInstance<MicrophoneLayer>().size + 1
                                if (isAllTrackRecording) return@clickable
                                startMicrophoneRecording(
                                    File(context.filesDir, "$number.mp3").absolutePath
                                )
                            } else {
                                launcher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        } else {
                            stopMicrophoneRecording()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_microphone),
                    contentDescription = null,
                    tint = if (isMicrophoneRecording)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.background
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .size(BOTTOM_BAR_SIZE.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        if (!isAllTrackRecording) {
                            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.RECORD_AUDIO
                                )
                            ) {
                                if (isMicrophoneRecording) return@clickable
                                startAllTrackRecording(
                                    File(context.filesDir, RECORD_TRACK_FILE_NAME).absolutePath
                                )
                            } else {
                                launcher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        } else {
                            stopAllTrackRecording()
                            val shareIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_STREAM,
                                    FileProvider.getUriForFile(
                                        context,
                                        context.packageName + ".provider",
                                        File(context.filesDir, RECORD_TRACK_FILE_NAME)
                                    )
                                )
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                type = "audio/*"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, null))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_record),
                    contentDescription = null,
                    tint = if (isAllTrackRecording)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.background
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .size(BOTTOM_BAR_SIZE.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        if (isAllTrackPlaying) {
                            stopAllTrackPlaying()
                        } else {
                            startAllTrackPlaying()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = if (isAllTrackPlaying)
                        ImageVector.vectorResource(R.drawable.ic_pause)
                    else
                        ImageVector.vectorResource(R.drawable.ic_play),
                    contentDescription = null
                )
            }
        }
    }
}