package com.devansh.noteapp.core.designsystem.components.colorPickerBottomSheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.mohamedrejeb.calf.ui.sheet.AdaptiveBottomSheet
import com.mohamedrejeb.calf.ui.sheet.AdaptiveSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerBottomSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentColor: Long?,
    onColorChanged: (Long?) -> Unit,
    sheetState: AdaptiveSheetState,
    controller: ColorPickerController
) {
    if (isOpen) {
        AdaptiveBottomSheet(
            onDismissRequest = onDismiss,
            adaptiveSheetState = sheetState,
//            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            ColorPickerContent(
                currentColor = currentColor,
                onColorChanged = onColorChanged,
                controller = controller
            )
        }
    }
}
