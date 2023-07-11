package sample.gthio.tasks.data.source

import sample.gthio.tasks.data.model.DataGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.domain.model.DomainGroup
import java.util.*

interface GroupLocalSource {
    val groups: Flow<List<DataGroup>>

    suspend fun insert(group: DomainGroup)

    suspend fun deleteById(id: UUID)
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
}