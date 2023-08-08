package sample.gthio.tasks.ui.route.home.editgroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import sample.gthio.tasks.domain.model.toColor
import sample.gthio.tasks.ui.route.addgroup.AddGroupColorInputList
import sample.gthio.tasks.ui.route.addgroup.AddGroupTitleEditText

@Composable
fun EditGroupDialog(
    state: EditGroupState,
    onEvent: (EditGroupEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(EditGroupEvent.CloseWithoutSaving) },
        icon = { Icon(Icons.Default.Edit, contentDescription = "edit group")},
        title = { Text(text = "Edit Group") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AddGroupTitleEditText(
                    color = state.groupColor.toColor(),
                    title = state.groupTitle,
                    onValueChange = { title -> onEvent(EditGroupEvent.TitleValueChange(title))}
                )
                AddGroupColorInputList(
                    availableColors = state.availableGroupColors.map { it.toColor() },
                    onColorSelect = { color -> onEvent(EditGroupEvent.SelectColor(color)) }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onEvent(EditGroupEvent.SaveButtonClick) }
            ) {
                Text("Save")
            }
        }
    )
}