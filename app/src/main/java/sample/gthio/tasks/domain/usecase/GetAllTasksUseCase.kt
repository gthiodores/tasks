package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.repository.TaskRepository

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<DomainTask>> {
        return taskRepository.tasks
    }
}