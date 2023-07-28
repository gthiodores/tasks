package sample.gthio.tasks.data.model

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.GroupColor
import java.util.*

data class DataGroup(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val colorCode: Int,
) {
    fun toDomain(): DomainGroup = DomainGroup(
        id = id,
        title = title,
        groupColor = GroupColor.from(colorCode)
    )

    companion object {
        fun from(group: DomainGroup): DataGroup = DataGroup(
            id = group.id,
            title = group.title,
            colorCode = group.groupColor.id
        )
    }
}