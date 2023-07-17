package sample.gthio.tasks.domain.repository

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainGroup
import java.util.*

interface GroupRepository {
    val groups: Flow<List<DomainGroup>>

    suspend fun insertGroup(group: DomainGroup)

    suspend fun deleteGroupById(id: UUID)

    suspend fun updateGroup(group: DomainGroup)

    suspend fun existsById(id: UUID): Boolean

    suspend fun observeById(id: UUID): Flow<DomainGroup?>
}