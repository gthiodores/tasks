package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.repository.TaskRepository
import java.util.UUID

class ObserveTaskByTagUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(tagId: UUID): Flow<List<DomainTask>> {
        return taskRepository.getTaskByTagId(tagId)
    }
}