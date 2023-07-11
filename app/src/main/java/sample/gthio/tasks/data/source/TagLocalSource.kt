package sample.gthio.tasks.data.source

import sample.gthio.tasks.data.model.DataTag
import sample.gthio.tasks.domain.model.DomainTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

interface TagLocalSource {
    val tags: Flow<List<DataTag>>

    suspend fun insert(tag: DomainTag)

    suspend fun deleteById(id: UUID)
}

fun inMemoryTagSource(): TagLocalSource = object : TagLocalSource {
    private val _tags = MutableStateFlow<List<DataTag>>(emptyList())
    override val tags: Flow<List<DataTag>> = _tags.asStateFlow()

    override suspend fun insert(tag: DomainTag) {
        _tags.update { tags -> tags + DataTag.from(tag) }
    }

    override suspend fun deleteById(id: UUID) {
        _tags.update { tags -> tags.filterNot { tag -> tag.id == id } }
    }
}