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
        .get<String?>("filterQuery")
        .let { query -> TaskFilterQuery.fromArg(query.orEmpty()) }

    private val groupIdFromArgs = savedStateHandle
        .get<String?>("groupId")
        .let { groupId -> groupId?.let(UUID::fromString) }

    private val tagIdFromArgs = savedStateHandle
        .get<String?>("tagId")
        .let { tagId -> tagId?.let(UUID::fromString) }

    private val _inputState = MutableStateFlow(
        TaskListInputState(
            filterQuery = queryFromArgs,
            selectedGroupId = if (groupIdFromArgs != null) listOf(groupIdFromArgs) else emptyList(),
            selectedTagId = if (tagIdFromArgs != null) listOf(tagIdFromArgs) else emptyList()
        )
    )

    private val _tasks = _inputState.flatMapLatest { inputState ->
        val taskQueries: MutableList<TaskQuery> = mutableListOf()

        when (inputState.filterQuery) {
            TaskFilterQuery.ALL -> {}
            TaskFilterQuery.TODAY -> taskQueries += TaskQuery.isToday
            TaskFilterQuery.IMPORTANT -> taskQueries += TaskQuery.IsImportant
            TaskFilterQuery.SCHEDULED -> {}
        }

        if (inputState.selectedGroupId.isNotEmpty())
            taskQueries += TaskQuery.HasGroupWithId(inputState.selectedGroupId)
        if (inputState.selectedTagId.isNotEmpty())
            taskQueries += TaskQuery.HasTagWithId(inputState.selectedTagId)

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
        _inputState
    ) { tasks, groups, tags, inputState ->
        TaskListUiState(
            tasks = tasks.filter { task -> !task.isFinished },
            completedTasks = tasks.filter { task -> task.isFinished },
            groups = groups,
            tags = tags,
            selectedTagId = inputState.selectedTagId,
            selectedGroupId = inputState.selectedGroupId,
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