package sample.gthio.tasks.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import sample.gthio.tasks.domain.model.DomainTask
import java.util.*

data class DataTask(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val timestamp: Timestamp,
    val tags: List<DataTag> = emptyList(),
    val isImportant: Boolean = false,
    val group: DataGroup
) {
    fun toDomain(): DataTask = DataTask(
        id = id,
        title = title,
        description = description,
        timestamp = timestamp,
        tags = tags,
        isImportant = isImportant,
        group = group
    )

    companion object {
        fun from(task: DomainTask): DataTask {
            return DataTask(
                id = task.id,
                title = task.title,
                description = task.description,
                tags = task.tags.map { tag -> DataTag.from(tag) },
                isImportant = task.isImportant,
                group = DataGroup.from(task.group),
                timestamp = Timestamp
                    .fromInstant(
                        LocalDateTime(task.date, task.time).toInstant(TimeZone.currentSystemDefault())
                    ),
            )
        }
    }
}
