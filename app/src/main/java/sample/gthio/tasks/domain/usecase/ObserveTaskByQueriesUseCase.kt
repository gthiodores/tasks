package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.TaskQuery
import sample.gthio.tasks.domain.model.TaskQueryModel
import sample.gthio.tasks.domain.repository.TaskRepository

class ObserveTaskByQueriesUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(queries: List<TaskQuery>): Flow<List<DomainTask>> {
        return taskRepository.observeTaskByQueries(queries)
    }
}

class ObserveTaskWithQuery(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(query: TaskQueryModel): Flow<List<DomainTask>> {
        return taskRepository.observeTaskByQueryModel(query)
    }
}