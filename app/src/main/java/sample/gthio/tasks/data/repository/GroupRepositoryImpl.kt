package sample.gthio.tasks.data.repository

import sample.gthio.tasks.data.model.DataGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sample.gthio.tasks.data.source.GroupLocalSource
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.repository.GroupRepository
import java.util.*

fun defaultGroupRepository(groupSource: GroupLocalSource): GroupRepository = object : GroupRepository {
    override val groups: Flow<List<DomainGroup>>
        get() = groupSource
            .groups
            .map { value -> value.map(DataGroup::toDomain) }

    override suspend fun insertGroup(group: DomainGroup) {
        groupSource.insert(group)
    }

    override suspend fun deleteGroupById(id: UUID) {
        groupSource.deleteById(id)
    }
}