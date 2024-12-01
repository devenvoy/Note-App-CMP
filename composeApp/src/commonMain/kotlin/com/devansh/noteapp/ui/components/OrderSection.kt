package com.devansh.noteapp.ui.components

/*

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(modifier = Modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title,
                onSelected = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = "Date",
                selected = noteOrder is NoteOrder.Date,
                onSelected = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = "Color",
                selected = noteOrder is NoteOrder.Color,
                onSelected = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Making second Row
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelected = {
                    onOrderChange(noteOrder.copyNoteHelper(OrderType.Descending))
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelected = {
                    onOrderChange(noteOrder.copyNoteHelper(OrderType.Ascending))
                }
            )
        }

    }
}
*/
