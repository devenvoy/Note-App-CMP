package com.devansh.noteapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devansh.noteapp.domain.utils.UnitCBF
import note_app_cmp.composeapp.generated.resources.Res
import note_app_cmp.composeapp.generated.resources.no_task_light
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun EmptyScreen(
    text: String,
    image: DrawableResource = Res.drawable.no_task_light,
    buttonState: Pair<String, UnitCBF>? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(image),
            contentDescription = text
        )
        Text(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(.8f),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

        buttonState?.let { (title, onClick) ->
            PrimaryButton(
                modifier = Modifier.widthIn(max = 200.dp, min = Dp.Infinity)
                    .height(100.dp)
                    .padding(vertical = 20.dp),
                onClick = onClick
            ) {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }
}