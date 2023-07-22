package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.first
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.repository.TaskRepository

class UpsertTaskUseCase(
   private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: DomainTask) {
        if (taskRepository.tasks.first().any { it.id == task.id }) {
            taskRepository.updateTask(task)
        } else {
            taskRepository.insertTask(task)
        }
    }
}