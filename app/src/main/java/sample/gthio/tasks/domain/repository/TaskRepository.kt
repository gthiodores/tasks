package sample.gthio.tasks.domain.repository

import sample.gthio.tasks.domain.model.DomainTask
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TaskRepository {
    val tasks: Flow<List<DomainTask>>
    
    suspend fun insertTask(task: DomainTask)
    
    suspend fun deleteTaskById(uuid: UUID)

    suspend fun updateTask(task: DomainTask)
}