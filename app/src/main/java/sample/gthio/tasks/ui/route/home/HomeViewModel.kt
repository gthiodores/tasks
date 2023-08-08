package sample.gthio.tasks.ui.route.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.model.toGroupColor
import sample.gthio.tasks.domain.usecase.ObserveAllGroupUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTagUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTaskUseCase
import sample.gthio.tasks.domain.usecase.ObserveAvailableGroupColorUseCase
import sample.gthio.tasks.domain.usecase.ObserveTaskByTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertGroupUseCase
import sample.gthio.tasks.ui.extension.updateNotNull
import sample.gthio.tasks.ui.model.UiGroup
import sample.gthio.tasks.ui.route.home.editgroup.EditGroupEvent
import sample.gthio.tasks.ui.route.home.editgroup.EditGroupState
import sample.gthio.tasks.ui.route.taskList.TaskFilterQuery
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeAllTask: ObserveAllTaskUseCase,
    private val observeTaskByTag: ObserveTaskByTagUseCase,
    private val upsertGroup: UpsertGroupUseCase,
    observeAvailableGroupColor: ObserveAvailableGroupColorUseCase,
    observeAllGroup: ObserveAllGroupUseCase,
    observeAllTag: ObserveAllTagUseCase,
) : ViewModel() {

    private val _navigation = MutableStateFlow<HomeNavigationTarget?>(null)
    val navigationTarget = _navigation.asStateFlow()

    private val _inputState = MutableStateFlow(HomeInputState())

    private val _tasks = _inputState
        .flatMapLatest { input ->
            when (val tag = input.selectedTagId) {
                null -> observeAllTask()
                else -> observeTaskByTag(tag)
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
        HomeUiState(
            tasks = tasks,
            groups = groups
                .map { group ->
                    UiGroup(
                        group = group,
                        quantity = tasks.count { task -> task.group == group }
                    )
                },
            tags = tags,
            selectedTag = inputState
                .selectedTagId
                ?.let { id -> tags.firstOrNull { tag -> tag.id == id } }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeUiState()
    )

    private val _availableGroupColor = observeAvailableGroupColor()

    private val _editGroupState = MutableStateFlow<EditGroupState?>(null)
    val editGroupState = _editGroupState
        .combine(_availableGroupColor) { editGroupState, availableColorGroup ->
            EditGroupState.fromInput(editGroupState, availableColorGroup)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SelectAllTags -> handleSelectAllTag()
            is HomeEvent.SelectTag -> handleSelectTag(event.tag)
            is HomeEvent.FabClick -> handleFabClick()
            is HomeEvent.AddClick -> handleAddClick()
            HomeEvent.AllTasksClick -> handleAllTasksClick()
            HomeEvent.ImportantClick -> handleImportantClick()
            HomeEvent.TodayClick -> handleTodayClick()
            is HomeEvent.GroupItemClick -> handleGroupItemClick(event.group)
            is HomeEvent.GroupItemLongClick -> handleGroupItemLongClick(event.group)
        }
    }

    private fun handleSelectAllTag() {
        _inputState.update { old -> old.copy(selectedTagId = null) }
    }

    private fun handleSelectTag(tag: DomainTag) {
        _inputState.update { old -> old.copy(selectedTagId = tag.id) }
    }

    private fun handleFabClick() {
        _navigation.update { HomeNavigationTarget.AddTask }
    }

    private fun handleAddClick() {
        _navigation.update { HomeNavigationTarget.AddGroup }
    }

    private fun handleAllTasksClick() {
        _navigation.update { HomeNavigationTarget.TaskList(TaskFilterQuery.ALL, null, uiState.value.selectedTag?.id) }
    }

    private fun handleGroupItemClick(group: DomainGroup) {
        _navigation.update { HomeNavigationTarget.TaskList(null, group.id, uiState.value.selectedTag?.id) }
    }

    private fun handleImportantClick() {
        _navigation.update { HomeNavigationTarget.TaskList(TaskFilterQuery.IMPORTANT, null, uiState.value.selectedTag?.id) }
    }

    private fun handleTodayClick() {
        _navigation.update { HomeNavigationTarget.TaskList(TaskFilterQuery.TODAY, null, uiState.value.selectedTag?.id) }
    }

    private fun handleGroupItemLongClick(group: DomainGroup) {
        _editGroupState.update {
            EditGroupState(
                groupId = group.id,
                groupTitle = group.title,
                groupColor = group.groupColor
            )
        }
    }

    fun onEditGroupEvent(event: EditGroupEvent) {
        when (event) {
            is EditGroupEvent.TitleValueChange -> handleEditGroupTitleValueChange(event.title)
            is EditGroupEvent.SelectColor -> handleEditGroupSelectColor(event.color)
            EditGroupEvent.CloseWithoutSaving -> handleEditGroupCloseWithoutSaving()
            EditGroupEvent.SaveButtonClick -> handleEditGroupSaveButtonClick()
        }
    }

    private fun handleEditGroupTitleValueChange(title: String) {
        _editGroupState.updateNotNull { old -> old.copy(groupTitle = title) }
    }

    private fun handleEditGroupSelectColor(color: Color) {
        _editGroupState.updateNotNull { old -> old.copy(groupColor = toGroupColor(color)!!) }
    }

    private fun handleEditGroupCloseWithoutSaving() {
        _editGroupState.update { null }
    }

    private fun handleEditGroupSaveButtonClick() {
        val editGroupState = editGroupState.value ?: return

        viewModelScope.launch {
            upsertGroup(
                DomainGroup(
                    id = editGroupState.groupId,
                    title = editGroupState.groupTitle,
                    groupColor = editGroupState.groupColor
                )
            )
        }

        _editGroupState.update { null }
    }

    fun homeNavigationDone() { _navigation.update { null } }
}