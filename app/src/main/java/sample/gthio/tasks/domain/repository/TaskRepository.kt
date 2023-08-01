package sample.gthio.tasks.domain.repository

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTask
import java.util.UUID

interface TaskRepository {
    val tasks: Flow<List<DomainTask>>
    
    suspend fun insertTask(task: DomainTask)
    
    suspend fun deleteTaskById(uuid: UUID)

    suspend fun updateTask(task: DomainTask)

    fun observeTaskByTagId(id: UUID): Flow<List<DomainTask>>

    fun observeTask(id: UUID): Flow<DomainTask?>

    fun observeTaskByTagAndGroup(tagId: UUID, groupId: UUID): Flow<List<DomainTask>>

    fun observeTaskByGroup(groupId: UUID): Flow<List<DomainTask>>
}