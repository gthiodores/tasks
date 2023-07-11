package sample.gthio.tasks.data.model

import sample.gthio.tasks.domain.model.DomainTag
import java.util.*

data class DataTag(
    val id: UUID = UUID.randomUUID(),
    val title: String,
) {
    fun toDomain() : DomainTag = DomainTag(
        id = id,
        title = title,
    )
    
    companion object {
        fun from(tag: DomainTag) : DataTag = DataTag(
            id = tag.id,
            title = tag.title,
        )
    }
}