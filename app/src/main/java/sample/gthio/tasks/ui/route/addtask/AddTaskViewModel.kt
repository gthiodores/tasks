package sample.gthio.tasks.ui.route.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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

    private val _groups = observeAllGroup()

    private val _tags = observeAllTag()

    private val _inputState = MutableStateFlow(AddTaskInputState())

    val uiState = combine(
        _groups,
        _tags,
        _inputState
    ) { groups, tags, inputState ->
       AddTaskUiState(
           groups = groups,
           tags = tags
       ).fromInputState(inputState)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AddTaskUiState()
    )



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
            is AddTaskEvent.SaveTime -> handleSaveTime(event.time)
            is AddTaskEvent.SaveDate -> handleSaveDate(event.dateInMillis)
            AddTaskEvent.DismissTime -> handleDismissTime()
        }
    }

    private fun handleBackPressed() {
        _inputState.update { old -> old.copy(shouldNavigateBack = true) }
    }

    private fun handleTitleValueChange(value: String) {
        _inputState.update { old -> old.copy(title = value) }
    }

    private fun handleDescriptionValueChange(value: String) {
        _inputState.update { old -> old.copy(description = value) }
    }

    private fun handleImportantSelect() {
        _inputState.update { old -> old.copy(isImportant = !old.isImportant) }
    }

    private fun handleGroupSelect(group: DomainGroup) {
        _inputState.update { old -> old.copy(selectedGroup = group) }
    }

    private fun handleTagSelect(tag: DomainTag) {
        _inputState.update { old ->
            old.copy(selectedTags = old.selectedTags
                .addOrRemoveDuplicate(tag) { a, b -> a == b } )
        }
    }

    private fun handleNewTagValueChange(newTag: String) {
        _inputState.update { old -> old.copy(newTag = newTag) }
    }

    private fun handleNewTagAddButtonClick(newTag: String) {
        viewModelScope.launch {
            upsertTag(tag = DomainTag(title = newTag))
            _inputState.update { old -> old.copy(newTag = "") }
        }
    }

    private fun handleSaveButtonClick() {
        viewModelScope.launch {
            if (_inputState.value.selectedGroup != null) {
                upsertTask(
                    DomainTask(
                        title = _inputState.value.title,
                        description = if (_inputState.value.description != "") _inputState.value.description else null,
                        tags = _inputState.value.selectedTags,
                        isImportant = _inputState.value.isImportant,
                        group = _inputState.value.selectedGroup!!,
                    )
                )
                _inputState.update { old -> old.copy(shouldNavigateBack = true) }
            }
        }
    }

    private fun handleOpenDate() {
        _inputState.update { old -> old.copy(isDateOpen = !old.isDateOpen) }
    }

    private fun handleOpenTag() {
        _inputState.update { old -> old.copy(isTagOpen = !old.isTagOpen) }
    }

    private fun handleOpenTime() {
        _inputState.update { old -> old.copy(isTimeOpen = true) }
    }

    private fun handleSaveTime(time: LocalTime) {
        _inputState.update { old ->
            old.copy(
                isTimeOpen = false,
                time = time,
            )
        }
    }

    private fun handleSaveDate(long: Long) {
        _inputState.update { old ->
            old.copy(
                date = Instant
                    .fromEpochMilliseconds(long)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
            )
        }
    }

    private fun handleDismissTime() {
        _inputState.update { old -> old.copy(isTimeOpen = false) }
    }

    fun addTaskNavigationDone() { _inputState.update { old -> old.copy(shouldNavigateBack = false) } }
}