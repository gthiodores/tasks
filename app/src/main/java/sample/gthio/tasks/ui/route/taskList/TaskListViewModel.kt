package sample.gthio.tasks.ui.route.taskList

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.usecase.ObserveAllGroupUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTaskUseCase
import sample.gthio.tasks.ui.extension.toDateString
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TaskListViewModel @Inject constructor(
    observeAllTask: ObserveAllTaskUseCase,
    observeAllGroup: ObserveAllGroupUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val queryFromArgs = savedStateHandle
        .getStateFlow<String>("query", "")
        .map { it }

    private val groupIdFromArgs = savedStateHandle
        .getStateFlow<String>("groupId", "")
        .map { it }

    private val _inputState = MutableStateFlow(TaskListInputState())

    private val _tasks = observeAllTask()

    private val _groups = observeAllGroup()

    val uiState = combine(
        queryFromArgs,
        groupIdFromArgs,
        _tasks,
        _groups,
        _inputState
    ) { query, groupId, tasks, groups, inputState ->
        TaskListUiState(
            tasks = filterTasks(query, groupId, tasks)
                .sortedWith(compareBy<DomainTask> { it.date }.thenBy{ it.time })
                .groupBy { it.date.toDateString() },
            groups = groups,
            query = inputState.query ?: query,
            selectedGroupId = inputState.selectedGroupId ?: if (groupId.isNotEmpty()) UUID.fromString(groupId) else null,
            shouldNavigateBack = inputState.shouldNavigateBack
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TaskListUiState()
    )

    private fun filterTasks(query: String, groupId: String, tasks: List<DomainTask>): List<DomainTask> {
        Log.d("TAG", query)
        return when (query) {
            "" -> tasks.filter { task -> task.group.id.toString() == groupId }
            "all_task" -> tasks
            "important" -> tasks.filter { task -> task.isImportant }
            "today" -> tasks.filter { task -> task.date == Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
            else -> tasks
        }
    }

    fun onEvent(event: TaskListEvent) {
        when (event) {
            TaskListEvent.BackPressed -> handleBackPressed()
        }
    }

    private fun handleBackPressed() {
        _inputState.update { old -> old.copy(shouldNavigateBack = true) }
    }

}