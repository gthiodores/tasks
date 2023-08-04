package sample.gthio.tasks.ui.route.taskList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.model.TaskQuery
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TaskListViewModel @Inject constructor(
    observeAllTask: ObserveAllTaskUseCase,
    observeAllTaskByQueries: ObserveTaskByQueriesUseCase,
    observeAllGroup: ObserveAllGroupUseCase,
    observeAllTag: ObserveAllTagUseCase,
    private val upsertTask: UpsertTaskUseCase,
    savedStateHandle: SavedStateHandle,
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

    private val _inputState = MutableStateFlow(TaskListInputState())

    private val filterState = combine(
        queryFromArgs,
        groupIdFromArgs,
        tagIdFromArgs,
        _inputState
    ) { filterQuery, group, tag, inputState ->
        TaskListFilterState(
            filterQuery = filterQuery,
            selectedGroupId = if (group != null) inputState.selectedGroupId + group else inputState.selectedGroupId,
            selectedTagId = if (tag != null) inputState.selectedTagId + tag else inputState.selectedTagId
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TaskListFilterState()
    )

    private val _tasks = filterState.flatMapLatest { filterState ->
        val taskQueries: MutableList<TaskQuery> = mutableListOf()

        when (filterState.filterQuery) {
            TaskFilterQuery.ALL -> {}
            TaskFilterQuery.TODAY -> taskQueries += TaskQuery.isToday
            TaskFilterQuery.IMPORTANT -> taskQueries += TaskQuery.IsImportant
            TaskFilterQuery.SCHEDULED -> {}
        }

        if (filterState.selectedGroupId.isNotEmpty())
            taskQueries += TaskQuery.HasGroupWithId(filterState.selectedGroupId)
        if (filterState.selectedTagId.isNotEmpty())
            taskQueries += TaskQuery.HasTagWithId(filterState.selectedTagId)

        when (taskQueries) {
            emptyList<TaskQuery>() -> observeAllTask()
            else -> observeAllTaskByQueries(taskQueries)
        }
    }

    private val _groups = observeAllGroup()

    private val _tags = observeAllTag()

    val uiState = combine(
        _tasks,
        _groups,
        _tags,
        _inputState,
        filterState
    ) { tasks, groups, tags, inputState, filterState ->
        TaskListUiState(
            tasks = tasks.filter { task -> !task.isFinished },
            completedTasks = tasks.filter { task -> task.isFinished },
            groups = groups,
            tags = tags,
            selectedTagId = filterState.selectedTagId,
            selectedGroupId = filterState.selectedGroupId,
            isFilterOpen = inputState.isFilterOpen,
            shouldNavigateBack = inputState.shouldNavigateBack,
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
            TaskListEvent.FilterAllTag -> handleFilterAllTag()
            is TaskListEvent.FilterByTag -> handleFilterByTag(event.tagId)
            is TaskListEvent.FilterByGroup -> handleFilterByGroup(event.groupId)
            TaskListEvent.FilterButtonClick -> handleFilterButtonClick()
            TaskListEvent.DismissFilter -> handleDismissFilter()
            TaskListEvent.SaveFilter -> handleSaveFilter()
            TaskListEvent.ResetFilter -> handleResetFilter()
        }
    }

    private fun handleFilterAllTag() {
        _inputState.update { old -> old.copy(selectedTagId = emptyList()) }
    }

    private fun handleSaveFilter() {
        _inputState.update { old -> old.copy(isFilterOpen = false) }
    }

    private fun handleDismissFilter() {
        _inputState.update { old -> old.copy(isFilterOpen = false) }
    }

    private fun handleFilterButtonClick() {
        _inputState.update { old -> old.copy(isFilterOpen = true) }
    }

    private fun handleResetFilter() {
        _inputState.update { old ->
            old.copy(selectedTagId = emptyList(), selectedGroupId = emptyList())
        }
    }

    private fun handleFilterByTag(tagId: UUID) {
        _inputState.update { old -> old.selectTag(tagId) }
    }

    private fun handleFilterByGroup(groupId: UUID) {
        _inputState.update { old -> old.selectGroup(groupId) }
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