package sample.gthio.tasks.data.repository

import sample.gthio.tasks.data.model.DataTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sample.gthio.tasks.data.source.TagLocalSource
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.repository.TagRepository
import java.util.*

fun defaultTagRepository(tagSource: TagLocalSource): TagRepository = object : TagRepository {
    override val tag: Flow<List<DomainTag>>
        get() = tagSource
            .tags
            .map { tags -> tags.map(DataTag::toDomain) }

    override suspend fun insertTag(tag: DomainTag) {
        tagSource.insert(tag)
    }

    override suspend fun deleteTagById(uuid: UUID) {
        tagSource.deleteById(uuid)
    }

    override suspend fun updateTag(tag: DomainTag) {
        tagSource.update(tag)
    }


}
