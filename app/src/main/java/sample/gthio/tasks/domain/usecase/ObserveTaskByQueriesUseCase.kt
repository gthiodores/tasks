package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.TaskQuery
import sample.gthio.tasks.domain.repository.TaskRepository

class ObserveTaskByQueriesUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(queries: List<TaskQuery>): Flow<List<DomainTask>> {
        return taskRepository.observeTaskByQueries(queries)
    }
}