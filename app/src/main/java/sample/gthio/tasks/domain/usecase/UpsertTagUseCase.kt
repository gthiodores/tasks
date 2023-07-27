package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.first
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.repository.TagRepository
import sample.gthio.tasks.domain.repository.TaskRepository

class UpsertTagUseCase(
    private val taskRepository: TaskRepository,
    private val tagRepository: TagRepository,
) {
    suspend operator fun invoke(tag: DomainTag) {
        if (tagRepository.tag.first().any { it.id == tag.id || it.title == tag.title}) {
            tagRepository.updateTag(tag)
            taskRepository.tasks.collect { tasks ->
                tasks.forEach { task ->
                    if (task.tags.any { it.id == tag.id }) {
                        taskRepository.updateTask(
                            task.copy(tags = task.tags.map { if(it.id == tag.id) tag else it })
                        )
                    }
                }
            }
        } else {
            tagRepository.insertTag(tag)
        }
    }
}