package sample.gthio.tasks.data.model

import sample.gthio.tasks.domain.model.DomainGroup
import java.util.*

data class DataGroup(
    val id: UUID = UUID.randomUUID(),
    val title: String,
) {
    fun toDomain(): DomainGroup = DomainGroup(
        id = id,
        title = title
    )

    companion object {
        fun from(group: DomainGroup): DataGroup = DataGroup(
            id = group.id,
            title = group.title
        )
    }
}