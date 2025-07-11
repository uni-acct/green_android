package com.blockstream.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import blockstream_green.common.generated.resources.Res
import com.blockstream.compose.LocalPreview
import com.blockstream.compose.R

@Composable
actual fun Rive(riveAnimation: RiveAnimation) {
    val path = when (riveAnimation) {
        RiveAnimation.LIGHTNING_TRANSACTION -> "lightning_transaction.riv"
        RiveAnimation.ROCKET -> "rocket.riv"
        RiveAnimation.ACCOUNT_ARCHIVED -> "account_archived.riv"
        RiveAnimation.CHECKMARK -> "checkmark.riv"
        RiveAnimation.WALLET -> "wallet.riv"
        RiveAnimation.RECOVERY_PHRASE -> "recovery_phrase.riv"
        RiveAnimation.GREEN_TO_BLOCKSTREAM -> "green_to_blockstream.riv"
        RiveAnimation.CREATE_WALLET -> "create_wallet.riv"
        RiveAnimation.JADE_BUTTON -> "jade_button.riv"
        RiveAnimation.JADE_SCROLL -> "jade_scroll.riv"
        RiveAnimation.JADE_POWER -> "jade_power.riv"
        RiveAnimation.JADE_UPDATE -> "jade_power.riv"
        RiveAnimation.JADE_PLUS_BUTTON -> "jade_plus_button.riv"
        RiveAnimation.JADE_PLUS_SCROLL -> "jade_plus_scroll.riv"
        RiveAnimation.JADE_PLUS_POWER -> "jade_plus_power.riv"
        RiveAnimation.JADE_PLUS_UPDATE -> "jade_plus_update.riv"
        RiveAnimation.NONE -> null
    }?.let {
        "files/rive/$it"
    }

    var bytes: ByteArray? by remember {
        mutableStateOf(null)
    }

    val isPreview = LocalInspectionMode.current || LocalPreview.current

    if (isPreview) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(BorderStroke(1.dp, Color.Gray))
        ) {
            Text(text = "Rive Preview", modifier = Modifier.align(Alignment.Center))
        }
    } else {
        AndroidView(factory = {
            RiveAnimationView(context = it).also {
                // For some reason if you don't init here rive with a resource, setRiveBytes is not working
                it.setRiveResource(R.raw.rive_empty)
            }
        }, update = {
            bytes?.also { bytes ->
                it.setRiveBytes(bytes = bytes, autoplay = true)
            }
        })

        LaunchedEffect(path) {
            bytes = path?.let { Res.readBytes(it) }
        }
    }
}