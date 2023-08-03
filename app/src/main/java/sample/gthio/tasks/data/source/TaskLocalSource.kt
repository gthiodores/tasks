package sample.gthio.tasks.data.source

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import sample.gthio.tasks.data.model.DataTask
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.TaskQuery
import java.time.LocalDate
import java.util.*

interface TaskLocalSource {
    val tasks: Flow<List<DataTask>>

    suspend fun insert(task: DomainTask)

    suspend fun deleteById(id: UUID)

    suspend fun update(task: DomainTask)

    fun observeTaskByTagId(id: UUID): Flow<List<DataTask>>

    fun observeTask(id: UUID): Flow<DataTask?>

    fun observeTaskByTagAndGroup(tagId: UUID, groupId: UUID): Flow<List<DataTask>>

    fun observeTaskByGroup(groupId: UUID): Flow<List<DataTask>>

    fun observeTaskByQueries(queries: List<TaskQuery>): Flow<List<DataTask>>
}

@OptIn(ExperimentalCoroutinesApi::class)
fun inMemoryTaskSource(): TaskLocalSource = object : TaskLocalSource {
    private val _task = MutableStateFlow<List<DataTask>>(emptyList())
    override val tasks: Flow<List<DataTask>> = _task.asStateFlow()

    override suspend fun insert(task: DomainTask) {
        _task.update { tasks -> tasks + DataTask.from(task) }
    }

    override suspend fun deleteById(id: UUID) {
        _task.update { tasks -> tasks.filterNot { task -> task.id == id } }
    }

    override suspend fun update(task: DomainTask) {
        _task.update { tasks -> tasks.map { if (it.id == task.id) DataTask.from(task) else it } }
    }

    override fun observeTaskByTagId(id: UUID): Flow<List<DataTask>> {
        return tasks
            .map { taskList ->
                taskList.filter { task -> task.tags.any { tag -> tag.id == id } }
            }
    }

    override fun observeTask(id: UUID): Flow<DataTask?> {
        return tasks
            .map { taskList -> taskList.firstOrNull { item -> item.id == id } }
    }

    override fun observeTaskByTagAndGroup(tagId: UUID, groupId: UUID): Flow<List<DataTask>> {
        return tasks
            .map { taskList ->
                taskList.filter { task ->
                    task.tags.any { tag -> tag.id == tagId } && task.group.id == groupId
                }
            }
    }

    override fun observeTaskByGroup(groupId: UUID): Flow<List<DataTask>> {
        return tasks
            .map { taskList -> taskList.filter { task -> task.group.id == groupId } }
    }

    override fun observeTaskByQueries(queries: List<TaskQuery>): Flow<List<DataTask>> {
        return tasks
            .map { taskList ->
                var result = taskList
                queries.forEach { query ->
                    result = when (query) {
                        TaskQuery.isToday -> result.filter { task ->
                            task.timestamp.toInstant()
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date == LocalDate.now()
                                .toKotlinLocalDate()
                        }

                        TaskQuery.IsImportant -> result.filter { task -> task.isImportant }
                        is TaskQuery.HasGroupWithId -> result.filter { task -> task.group.id == query.id }
                        is TaskQuery.HasTagWithId -> result.filter { task -> task.tags.any { tag -> tag.id == query.id } }
                    }
                }
                return@map result
            }

    }
}