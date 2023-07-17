package sample.gthio.tasks.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.data.model.DataGroup
import sample.gthio.tasks.domain.model.DomainGroup
import java.util.UUID

interface GroupLocalSource {
    val groups: Flow<List<DataGroup>>

    suspend fun insert(group: DomainGroup)

    suspend fun deleteById(id: UUID)

    suspend fun update(group: DomainGroup)
}

fun inMemoryGroupSource(): GroupLocalSource = object : GroupLocalSource {
    private val _group = MutableStateFlow<List<DataGroup>>(emptyList())
    override val groups: Flow<List<DataGroup>> = _group.asStateFlow()

    override suspend fun insert(group: DomainGroup) {
        _group.update { groups -> groups + DataGroup.from(group) }
    }

    override suspend fun deleteById(id: UUID) {
        _group.update { groups -> groups.filterNot { group -> group.id == id } }
    }

    override suspend fun update(group: DomainGroup) {
        _group.update { groups -> groups.map { if (it.id == group.id) DataGroup.from(group) else it } }
    }


}