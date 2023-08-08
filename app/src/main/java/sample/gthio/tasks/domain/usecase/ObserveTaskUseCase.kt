package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.repository.TaskRepository
import java.util.UUID

class ObserveTaskUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(id: UUID): Flow<DomainTask?> {
        return taskRepository.observeTask(id)
    }
}