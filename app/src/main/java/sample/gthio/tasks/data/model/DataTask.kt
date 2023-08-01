package sample.gthio.tasks.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import sample.gthio.tasks.domain.model.DomainTask
import java.util.*

data class DataTask(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val timestamp: Timestamp,
    val tags: List<DataTag> = emptyList(),
    val isImportant: Boolean = false,
    val group: DataGroup,
    val isFinished: Boolean = false,
) {
    fun toDomain(): DomainTask = DomainTask(
        id = id,
        title = title,
        description = description,
        date = timestamp.toInstant().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        time = timestamp.toInstant().toLocalDateTime(TimeZone.currentSystemDefault()).time,
        tags = tags.map { tag -> tag.toDomain() },
        isImportant = isImportant,
        group = group.toDomain(),
        isFinished = isFinished
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
                isFinished = task.isFinished
            )
        }
    }
}
