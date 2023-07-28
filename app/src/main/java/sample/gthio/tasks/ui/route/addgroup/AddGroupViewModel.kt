package sample.gthio.tasks.ui.route.addgroup

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.domain.model.GroupColor
import sample.gthio.tasks.domain.model.toColor
import sample.gthio.tasks.domain.usecase.ObserveAllGroupUseCase
import sample.gthio.tasks.domain.usecase.UpsertGroupUseCase
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    observeAllGroup: ObserveAllGroupUseCase,
    private val upsertGroup: UpsertGroupUseCase
): ViewModel() {

    private val _groups = observeAllGroup()

    private val _inputState = MutableStateFlow(AddGroupInputState())

    val uiState = combine(
        _groups,
        _inputState
    ) { groups, inputState ->
        AddGroupUiState(
            groups = groups,
            availableColors = groups
                .map { group -> group.groupColor }
                .filterNot { color -> GroupColor.values().any { it == color } }
                .map { groupColor -> groupColor.toColor() },
            title = inputState.title,
            selectedGroupColor = inputState.selectedGroupColor
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AddGroupUiState()
    )

    fun onEvent(event: AddGroupEvent) {
        when (event) {
            is AddGroupEvent.SelectColor -> handleSelectColor(event.color)
            is AddGroupEvent.TitleValueChange -> handleTitleValueChange(event.title)
        }
    }

    private fun handleSelectColor(color: Color) {
        _inputState.update { old -> old.copy(selectedGroupColor = color) }
    }

    private fun handleTitleValueChange(title: String) {
        _inputState.update { old -> old.copy(title = title) }
    }
}