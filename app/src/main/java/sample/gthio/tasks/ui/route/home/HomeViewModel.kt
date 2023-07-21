package sample.gthio.tasks.ui.route.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.usecase.GetAllGroupsUseCase
import sample.gthio.tasks.domain.usecase.GetAllTagsUseCase
import sample.gthio.tasks.domain.usecase.GetAllTasksUseCase
import sample.gthio.tasks.domain.usecase.GetTaskByTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertGroupUseCase
import sample.gthio.tasks.domain.usecase.UpsertTagUseCase
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTask: GetAllTasksUseCase,
    private val getTaskByTag: GetTaskByTagUseCase,
    private val getAllGroup: GetAllGroupsUseCase,
    private val getAllTag: GetAllTagsUseCase,
    private val upsertTag: UpsertTagUseCase,
    private val upsertGroup: UpsertGroupUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val uiState = _state.asStateFlow()

    val tasks = getAllTask()
        .onEach { tasks -> _state.update { old -> old.copy(tasks = tasks) } }

    val groups = getAllGroup()
        .onEach { groups -> _state.update { old -> old.copy(groups = groups) } }

    val tags = getAllTag()
        .onEach { tags -> _state.update { old -> old.copy(tags = tags) } }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SelectAllTags -> handleSelectAllTag()
            is HomeEvent.SelectTag -> handleSelectTag(event.tag)
            is HomeEvent.FabClick -> handleFabClick()
            is HomeEvent.AddClick -> handleAddClick()
        }
    }

    private fun handleSelectAllTag() {
        viewModelScope.launch {
            val tasks = getAllTask().first()
            _state.update { old ->
                old.copy(selectedTag = null, tasks = tasks)
            }
        }
    }

    private fun handleSelectTag(tag: DomainTag) {
        viewModelScope.launch {
            val tasks = getTaskByTag(tag.id).first()
            _state.update { old ->
                old.copy(selectedTag = tag, tasks = tasks)
            }
        }
    }

    private fun handleFabClick() {
        viewModelScope.launch {
            upsertTag(DomainTag(title = UUID.randomUUID().toString().split("-")[0]))
        }
    }

    private fun handleAddClick() {
        viewModelScope.launch {
            upsertGroup(DomainGroup(title = UUID.randomUUID().toString().split("-")[0]))
        }
    }
}