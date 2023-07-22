package sample.gthio.tasks.data.source

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.data.model.DataTask
import sample.gthio.tasks.domain.model.DomainTask
import java.util.*

interface TaskLocalSource {
    val tasks: Flow<List<DataTask>>

    suspend fun insert(task: DomainTask)

    suspend fun deleteById(id: UUID)

    suspend fun update(task: DomainTask)

    fun observeTaskByTagId(id: UUID): Flow<List<DataTask>>
}

@OptIn(ExperimentalCoroutinesApi::class)
fun inMemoryTaskSource(): TaskLocalSource = object : TaskLocalSource {
    private val _task = MutableStateFlow<List<DataTask>>(emptyList())
    override val tasks: Flow<List<DataTask>> = _task.asStateFlow()

    override suspend fun insert(task: DomainTask) {
        _task.update { tasks -> tasks + DataTask.from(task) }
    }

    override suspend fun deleteById(id: UUID) {
        _task.update { tasks -> tasks.filterNot { task -> task.id == id } }
    }

    override suspend fun update(task: DomainTask) {
        _task.update { tasks -> tasks.map { if (it.id == task.id) DataTask.from(task) else it } }
    }

    override fun observeTaskByTagId(id: UUID): Flow<List<DataTask>> {
        val tasksWithTag = tasks
            .flatMapLatest { tasks ->
                flowOf(tasks.filter { task -> task.tags.any { tag -> tag.id == id } })
            }
        return tasksWithTag
    }
}