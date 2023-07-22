package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.first
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.repository.GroupRepository
import sample.gthio.tasks.domain.repository.TaskRepository

class UpsertGroupUseCase(
    private val groupRepository: GroupRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(group: DomainGroup) {
        if (groupRepository.groups.first().any { it.id == group.id }) {
            groupRepository.updateGroup(group)
            taskRepository.tasks.collect { tasks ->
                tasks.forEach { task ->
                    if (task.group == group) {
                        taskRepository.updateTask(task.copy(group = group))
                    }
                }
            }
        } else {
            groupRepository.insertGroup(group)
        }
    }
}