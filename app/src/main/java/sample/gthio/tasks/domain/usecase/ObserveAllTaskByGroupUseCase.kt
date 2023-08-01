package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.repository.TaskRepository
import java.util.*

class ObserveAllTaskByGroupUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(groupId: UUID): Flow<List<DomainTask>> = repository
        .observeTaskByGroup(groupId = groupId)
}