package com.devansh.noteapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.dropShadow
//import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.core.util.DeviceConfiguration
import com.devansh.noteapp.domain.utils.UnitCBF
import com.devansh.noteapp.ui.components.button.PrimaryButton
import com.devansh.noteapp.ui.screens.base.LocalDeviceConfiguration
import noteapp.composeapp.generated.resources.Res
import noteapp.composeapp.generated.resources.no_task_light
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun EmptyScreen(
    text: String,
    image: DrawableResource = Res.drawable.no_task_light,
    buttonState: Pair<String, UnitCBF>? = null,
    modifier: Modifier = Modifier
) {
    val deviceConfiguration = LocalDeviceConfiguration.current

    val contentBlock = @Composable {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(image),
                contentDescription = text
            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(.8f),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    val contentButton = @Composable {
        buttonState?.let { (title, onClick) ->
            PrimaryButton(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(max = 200.dp, min = Dp.Infinity)
                    .height(60.dp)
                   /* .dropShadow(
                        shape = ButtonDefaults.shape,
                        shadow = Shadow(
                            radius = 15.dp,
                            spread = 8.dp,
                            color = MaterialTheme.colorScheme.primary.copy(.5f)
                        )
                    )*/,
                onClick = onClick,
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }

    if (deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            contentBlock()
            contentButton()
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            contentBlock()
            contentButton()
        }
    }
}