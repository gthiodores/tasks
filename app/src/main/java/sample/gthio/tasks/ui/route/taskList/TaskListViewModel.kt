package sample.gthio.tasks.ui.route.taskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.domain.usecase.ObserveAllTaskUseCase
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    observeAllTask: ObserveAllTaskUseCase
): ViewModel() {

    private val _tasks = observeAllTask()

    private val _inputState = MutableStateFlow(TaskListInputState())

    val uiState = combine(
        _tasks,
        _inputState
    ) { tasks, inputState ->
        TaskListUiState(
            tasks = if (inputState.selectedGroup != null)
                tasks.filter { task -> task.group == inputState.selectedGroup } else tasks,
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