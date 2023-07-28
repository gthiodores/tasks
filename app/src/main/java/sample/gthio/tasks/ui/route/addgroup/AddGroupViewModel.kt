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
import kotlinx.coroutines.launch
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.GroupColor
import sample.gthio.tasks.domain.model.toColor
import sample.gthio.tasks.domain.model.toGroupColor
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
            availableColors = GroupColor.values()
                .filterNot { groupColor -> groups.map { it.groupColor }.any { groupColor == it} }
                .map { groupColor -> groupColor.toColor() },
            title = inputState.title,
            selectedGroupColor = inputState.selectedGroupColor,
            shouldNavigateBack = inputState.shouldNavigateBack
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
            AddGroupEvent.BackPressed -> handleBackPressed()
            AddGroupEvent.CheckPressed -> handleCheckPressed()
        }
    }

    private fun handleSelectColor(color: Color) {
        _inputState.update { old -> old.copy(selectedGroupColor = color) }
    }

    private fun handleTitleValueChange(title: String) {
        _inputState.update { old -> old.copy(title = title) }
    }

    private fun handleBackPressed() {
        _inputState.update { old -> old.copy(shouldNavigateBack = true) }
    }

    private fun handleCheckPressed() {
        viewModelScope.launch {
            if (_inputState.value.selectedGroupColor != null || toGroupColor(_inputState.value.selectedGroupColor!!) != null) {
                upsertGroup(
                    DomainGroup(
                        title = _inputState.value.title,
                        groupColor = toGroupColor(_inputState.value.selectedGroupColor!!)!!
                    )
                )
                _inputState.update { old -> old.copy(shouldNavigateBack = true) }
            }
        }
    }
}