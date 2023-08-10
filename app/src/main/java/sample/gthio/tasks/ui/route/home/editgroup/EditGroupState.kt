package sample.gthio.tasks.ui.route.home.editgroup

import sample.gthio.tasks.domain.model.GroupColor
import java.util.UUID

data class EditGroupState(
    val groupId: UUID,
    val groupTitle: String,
    val groupColor: GroupColor,
    val availableGroupColors: List<GroupColor> = emptyList(),
) {
    companion object {
        fun fromInput(state: EditGroupState?, groupColors: List<GroupColor>): EditGroupState? {
            return state?.let { input ->
                EditGroupState(
                    groupId = input.groupId,
                    groupTitle = input.groupTitle,
                    groupColor = input.groupColor,
                    availableGroupColors = groupColors
                )
            }
        }
    }
}