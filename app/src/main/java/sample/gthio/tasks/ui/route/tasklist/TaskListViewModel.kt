package sample.gthio.tasks.ui.route.tasklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.domain.usecase.ObserveAllTaskUseCase
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    observeAllTask: ObserveAllTaskUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val groupIdFromArgs = savedStateHandle
        .getStateFlow<String?>("groupId", null)
        .map { it }

    private val _tasks = observeAllTask()

    private val _inputState = MutableStateFlow(TaskListInputState())

    val uiState = combine(
        groupIdFromArgs,
        _tasks,
        _inputState
    ) { groupId, tasks, inputState ->
        TaskListUiState(
            tasks = if (groupId != "null")
                tasks.filter { task -> task.group.id.toString() == groupId } else tasks,
            selectedGroup = inputState.selectedGroup,
            shouldNavigateBack = inputState.shouldNavigateBack
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TaskListUiState()
    )

    fun onEvent(event: TaskListEvent) {
        when (event) {
            TaskListEvent.BackPressed -> handleBackPressed()
        }
    }

    private fun handleBackPressed() {
        _inputState.update { old -> old.copy(shouldNavigateBack = true) }
    }

}