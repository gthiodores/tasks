package sample.gthio.tasks.data.source

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import sample.gthio.tasks.data.model.DataTask
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.TaskQuery
import sample.gthio.tasks.domain.model.TaskQueryModel
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

    fun observeTaskByQuery(model: TaskQueryModel): Flow<List<DataTask>>
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
                        is TaskQuery.HasGroupWithId -> result.filter { task -> query.ids.any { id -> task.group.id == id } }
                        is TaskQuery.HasTagWithId -> result.filter { task -> task.tags.any { tag -> query.id.any { id -> tag.id == id } } }
                    }
                }
                return@map result
            }
    }

    override fun observeTaskByQuery(model: TaskQueryModel): Flow<List<DataTask>> {
        if (model.isEmpty()) return tasks

        return tasks
            .mapIf(model.isImportant) { flow -> flow.filter { task -> task.isImportant } }
            .mapIf(model.isToday) { flow ->
                flow.filter { task -> task.timestamp.toInstant() >= Instant.getMidnightToday() }
            }
            .mapIf(model.hasGroupWithId.isNotEmpty()) { flow ->
                flow.filter { task -> task.group.id in model.hasGroupWithId }
            }
            .mapIf(model.hasTagWithId.isNotEmpty()) { flow ->
                flow.filter { task -> task.tags.any { tag -> tag.id in model.hasTagWithId } }
            }
    }
}

fun Instant.Companion.getMidnightToday(): Instant = Clock
    .System
    .now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date
    .atStartOfDayIn(TimeZone.currentSystemDefault())

fun <T> Flow<T>.mapIf(condition: Boolean, transform: (T) -> T): Flow<T> {
    return if (condition) {
        map { transform(it) }
    } else {
        this
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.flatMapLatestIf(condition: Boolean, transform: (T) -> Flow<T>): Flow<T> {
    return if (condition) {
        flatMapLatest { transform(it) }
    } else {
        this
    }
}