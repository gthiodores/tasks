package sample.gthio.tasks.domain.usecase

import sample.gthio.tasks.domain.repository.TaskRepository
import java.util.UUID
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: UUID) {
        taskRepository.deleteTaskById(taskId)
    }
}