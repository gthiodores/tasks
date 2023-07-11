package sample.gthio.tasks.data.source

import sample.gthio.tasks.data.model.DataTask
import sample.gthio.tasks.domain.model.DomainTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

interface TaskLocalSource {
    val tasks: Flow<List<DataTask>>

    suspend fun insert(task: DomainTask)

    suspend fun deleteById(id: UUID)
}

fun inMemoryTaskSource(): TaskLocalSource = object : TaskLocalSource {
    private val _task = MutableStateFlow<List<DataTask>>(emptyList())
    override val tasks: Flow<List<DataTask>> = _task.asStateFlow()

    override suspend fun insert(task: DomainTask) {
        _task.update { tasks -> tasks + DataTask.from(task) }
    }

    override suspend fun deleteById(id: UUID) {
        _task.update { tasks -> tasks.filterNot { task -> task.id == id } }
    }
}