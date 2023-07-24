package sample.gthio.tasks.ui.route.addtask

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.usecase.ObserveAllGroupUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertGroupUseCase
import sample.gthio.tasks.domain.usecase.UpsertTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertTaskUseCase
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val upsertTask: UpsertTaskUseCase,
    private val upsertTag: UpsertTagUseCase,
    private val upsertGroup: UpsertGroupUseCase,
    observeAllGroup: ObserveAllGroupUseCase,
    observeAllTag: ObserveAllTagUseCase
): ViewModel() {

    val groups = observeAllGroup()

    val tags = observeAllTag()

    private val _state = MutableStateFlow(AddTaskUiState())
    val uiState = _state.asStateFlow()

    fun onEvent(event: AddTaskEvent) {
        when (event) {
            is AddTaskEvent.BackPressed -> handleBackPressed()
            is AddTaskEvent.TitleValueChange -> handleTitleValueChange(event.value)
            is AddTaskEvent.DescriptionValueChange -> handleDescriptionValueChange(event.value)
            is AddTaskEvent.ImportantSelect -> handleImportantSelect()
            is AddTaskEvent.GroupSelect -> handleGroupSelect(event.group)
        }
    }

    private fun handleBackPressed() {
        _state.update { old -> old.copy(shouldNavigateBack = true) }
    }

    private fun handleTitleValueChange(value: String) {
        _state.update { old -> old.copy(title = value) }
    }

    private fun handleDescriptionValueChange(value: String) {
        _state.update { old -> old.copy(description = value) }
    }

    private fun handleImportantSelect() {
        _state.update { old -> old.copy(isImportant = !old.isImportant) }
    }

    private fun handleGroupSelect(group: DomainGroup) {
        _state.update { old -> old.copy(selectedGroup = group) }
    }
}