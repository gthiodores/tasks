package sample.gthio.tasks.ui.route.taskList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.usecase.*
import java.util.*
import javax.inject.Inject

enum class TaskFilterQuery(val arg: String) {
    ALL("all"),
    SCHEDULED("scheduled"),
    IMPORTANT("important"),
    TODAY("today");

    companion object {
        fun fromArg(arg: String): TaskFilterQuery {
            return TaskFilterQuery
                .values()
                .firstOrNull { query -> query.arg == arg }
                ?: ALL
        }
    }
}

sealed class TaskListQuery {
    abstract val filter: TaskFilterQuery
    abstract val groupId: UUID?
    abstract val tagId: UUID?

    data class QueryByGroup(
        override val filter: TaskFilterQuery,
        override val groupId: UUID,
    ) : TaskListQuery() {
        override val tagId: UUID? = null
    }

    data class QueryByTag(
        override val filter: TaskFilterQuery,
        override val tagId: UUID,
    ) : TaskListQuery() {
        override val groupId: UUID? = null
    }

    data class QueryByGroupAndTag(
        override val filter: TaskFilterQuery,
        override val groupId: UUID,
        override val tagId: UUID,
    ) : TaskListQuery()

    data class QueryByNone(
        override val filter: TaskFilterQuery
    ) : TaskListQuery() {
        override val groupId: UUID? = null
        override val tagId: UUID? = null
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TaskListViewModel @Inject constructor(
    observeAllTask: ObserveAllTaskUseCase,
    observeTaskByTag: ObserveTaskByTagUseCase,
    observeAllTaskByGroup: ObserveAllTaskByGroupUseCase,
    observeAllTaskByGroupAndTag: ObserveAllTaskByGroupAndTagUseCase,
    observeAllGroup: ObserveAllGroupUseCase,
    private val upsertTask: UpsertTaskUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val queryFromArgs = savedStateHandle
        .getStateFlow<String?>("filterQuery", null)
        .map { query -> TaskFilterQuery.fromArg(query.orEmpty()) }

    private val groupIdFromArgs = savedStateHandle
        .getStateFlow<String?>("groupId", null)
        .map { groupId -> groupId?.let(UUID::fromString) }

    private val tagIdFromArgs = savedStateHandle
        .getStateFlow<String?>("tagId", null)
        .map { tagId -> tagId?.let(UUID::fromString) }

    private val filterArg = combine(
        queryFromArgs,
        groupIdFromArgs,
        tagIdFromArgs,
    ) { filter, group, tag ->
        when {
            tag != null && group != null -> TaskListQuery.QueryByGroupAndTag(filter, group, tag)
            tag != null -> TaskListQuery.QueryByTag(filter, tag)
            group != null -> TaskListQuery.QueryByGroup(filter, group)
            else -> TaskListQuery.QueryByNone(filter)
        }
    }

    private val _tasks = filterArg.flatMapLatest { query ->
        when (query) {
            is TaskListQuery.QueryByGroup -> observeAllTaskByGroup(query.groupId).map { tasks -> tasks.filterTasks(query.filter) }
            is TaskListQuery.QueryByGroupAndTag -> observeAllTaskByGroupAndTag(query.groupId, query.tagId).map { tasks -> tasks.filterTasks(query.filter) }
            is TaskListQuery.QueryByNone -> observeAllTask().map { tasks -> tasks.filterTasks(query.filter) }
            is TaskListQuery.QueryByTag -> observeTaskByTag(query.tagId).map { tasks -> tasks.filterTasks(query.filter) }
        }
    }

    private fun List<DomainTask>.filterTasks(query: TaskFilterQuery): List<DomainTask> {
        return when (query) {
            TaskFilterQuery.ALL -> this
            TaskFilterQuery.IMPORTANT -> this.filter { task -> task.isImportant }
            TaskFilterQuery.TODAY -> this.filter { task -> task.date == Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
            TaskFilterQuery.SCHEDULED -> this
        }
    }

    private val _inputState = MutableStateFlow(TaskListInputState())

    private val _groups = observeAllGroup()

    val uiState = combine(
        _tasks,
        _groups,
        _inputState,
        filterArg,
    ) { tasks, groups, inputState, args ->
        TaskListUiState(
            tasks = tasks.filter { task -> !task.isFinished },
            completedTasks = tasks.filter { task -> task.isFinished },
            groups = groups,
            shouldNavigateBack = inputState.shouldNavigateBack,
            selectedTagId = args.tagId,
            selectedGroupId = args.groupId,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TaskListUiState()
    )

    fun onEvent(event: TaskListEvent) {
        when (event) {
            TaskListEvent.BackPressed -> handleBackPressed()
            is TaskListEvent.TaskFinishClick -> handleTaskFinishClick(event.task)
            is TaskListEvent.FilterByTag -> handleFilterByTag(event.tagId)
            is TaskListEvent.FilterByGroup -> handleFilterByGroup(event.groupId)
            TaskListEvent.ResetFilter -> handleResetFilter()
        }
    }

    private fun handleResetFilter() {
        savedStateHandle.remove<String?>("groupId")
        savedStateHandle.remove<String?>("tagId")
    }

    private fun handleFilterByTag(id: UUID?) {
        savedStateHandle["tagId"] = id?.toString()
    }

    private fun handleFilterByGroup(groupId: UUID?) {
        savedStateHandle["groupId"] = groupId?.toString()
    }

    private fun handleBackPressed() {
        _inputState.update { old -> old.copy(shouldNavigateBack = true) }
    }

    private fun handleTaskFinishClick(task: DomainTask) {
        viewModelScope.launch {
            upsertTask(
                task = task.copy(isFinished = !task.isFinished)
            )
        }
    }
}