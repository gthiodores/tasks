package sample.gthio.tasks.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import sample.gthio.tasks.data.model.DataTask
import sample.gthio.tasks.data.source.TaskLocalSource
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.repository.TaskRepository
import java.util.UUID

fun defaultTaskRepository(taskSource: TaskLocalSource): TaskRepository = object : TaskRepository {
    override val tasks: Flow<List<DomainTask>>
        get() = taskSource
            .tasks
            .map { tags -> tags.map(DataTask::toDomain) }

    override suspend fun insertTask(task: DomainTask) {
        taskSource.insert(task)
    }

    override suspend fun deleteTaskById(uuid: UUID) {
        taskSource.deleteById(uuid)
    }

    override suspend fun updateTask(task: DomainTask) {
        taskSource.update(task)
    }

    override fun observeTaskByTagId(id: UUID): Flow<List<DomainTask>> {
        return taskSource
            .observeTaskByTagId(id)
            .map { tasks -> tasks.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

}