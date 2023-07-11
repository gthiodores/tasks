package sample.gthio.tasks.domain.repository

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTag
import java.util.*

interface TagRepository {
    val tag: Flow<List<DomainTag>>

    suspend fun insertTag(tag: DomainTag)

    suspend fun deleteTagById(uuid: UUID)
}