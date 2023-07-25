package sample.gthio.tasks.ui.route.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.domain.usecase.ObserveAllGroupUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertGroupUseCase
import sample.gthio.tasks.domain.usecase.UpsertTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertTaskUseCase
import sample.gthio.tasks.ui.extension.addOrRemoveDuplicate
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
            is AddTaskEvent.TagSelect -> handleTagSelect(event.tag)
            is AddTaskEvent.NewTagValueChange -> handleNewTagValueChange(event.newTag)
            is AddTaskEvent.NewTagAddButtonClick -> handleNewTagAddButtonClick(event.newTag)
            is AddTaskEvent.GroupSelect -> handleGroupSelect(event.group)
            is AddTaskEvent.SaveButtonClick -> handleSaveButtonClick()
            AddTaskEvent.OpenDate -> handleOpenDate()
            AddTaskEvent.OpenTag -> handleOpenTag()
            AddTaskEvent.OpenTime -> handleOpenTime()
            AddTaskEvent.SaveTime -> handleSaveTime()
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

    private fun handleTagSelect(tag: DomainTag) {
        _state.update { old ->
            old.copy(selectedTags = old.selectedTags
                .addOrRemoveDuplicate(tag) { a, b -> a == b } )
        }
    }

    private fun handleNewTagValueChange(newTag: String) {
        _state.update { old -> old.copy(newTag = newTag) }
    }

    private fun handleNewTagAddButtonClick(newTag: String) {
        viewModelScope.launch {
            upsertTag(tag = DomainTag(title = newTag))
            _state.update { old -> old.copy(newTag = "") }
        }
    }

    private fun handleSaveButtonClick() {
        viewModelScope.launch {
            if (_state.value.selectedGroup != null) {
                upsertTask(
                    DomainTask(
                        title = _state.value.title,
                        description = if (_state.value.description != "") _state.value.description else null,
                        tags = _state.value.selectedTags,
                        isImportant = _state.value.isImportant,
                        group = _state.value.selectedGroup!!,
                    )
                )
                _state.update { old -> old.copy(shouldNavigateBack = true) }
            }
        }
    }

    private fun handleOpenDate() {
        _state.update { old -> old.copy(isDateOpen = !old.isDateOpen) }
    }

    private fun handleOpenTag() {
        _state.update { old -> old.copy(isTagOpen = !old.isTagOpen) }
    }

    private fun handleOpenTime() {
        _state.update { old -> old.copy(isTimeOpen = true) }
    }

    private fun handleSaveTime() {
        _state.update { old -> old.copy(isTimeOpen = false) }
    }

    fun addTaskNavigationDone() { _state.update { old -> old.copy(shouldNavigateBack = false) } }
}