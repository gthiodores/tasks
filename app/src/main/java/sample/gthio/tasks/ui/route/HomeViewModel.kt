package sample.gthio.tasks.ui.route

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.model.DomainTask

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val uiState = _state.asStateFlow()

    val tags = listOf(DomainTag(title = "Finance"), DomainTag(title = "Gaming"))
    private val groceriesGroup = DomainGroup(title = "Groceries")
    private val groups = listOf(DomainGroup(title = "Business"), groceriesGroup)
    val tasks = listOf(
        DomainTask(
            title = "Buy 3 Egg",
            group = groceriesGroup
        ),
        DomainTask(
            title = "Go to office",
            tags = tags,
            group = groceriesGroup
        )
    )

    init {
        _state.update { old ->
            old.copy(
                tags = tags,
                groups = groups,
                tasks = tasks
            )
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SelectAllTags -> handleSelectAllTag()
            is HomeEvent.SelectTag -> handleSelectTag(event.tag)
        }
    }

    private fun handleSelectAllTag() {
        _state.update { old ->
            old.copy(selectedTag = null)
        }
    }

    private fun handleSelectTag(tag: DomainTag) {
        _state.update { old ->
            old.copy(selectedTag = tag)
        }
    }
}