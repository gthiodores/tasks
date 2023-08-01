package sample.gthio.tasks.ui.route.home

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
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.usecase.ObserveAllGroupUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTagUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTaskUseCase
import sample.gthio.tasks.domain.usecase.ObserveTaskByTagUseCase
import sample.gthio.tasks.ui.model.UiGroup
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeAllTask: ObserveAllTaskUseCase,
    private val observeTaskByTag: ObserveTaskByTagUseCase,
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
        _navigation.update { HomeNavigationTarget.TaskList("all_tasks", "") }
    }

    private fun handleGroupItemClick(group: DomainGroup) {
        _navigation.update { HomeNavigationTarget.TaskList("", group.id.toString()) }
    }

    private fun handleImportantClick() {
        _navigation.update { HomeNavigationTarget.TaskList("important", "") }
    }

    private fun handleTodayClick() {
        _navigation.update { HomeNavigationTarget.TaskList("today", "") }
    }

    fun homeNavigationDone() { _navigation.update { null } }
}