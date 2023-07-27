package sample.gthio.tasks.ui.route.home

import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.ui.model.UiGroup
import java.util.UUID

data class HomeInputState(
    val selectedTagId: UUID? = null
)
data class HomeUiState(
    val tags: List<DomainTag> = emptyList(),
    val selectedTag: DomainTag? = null,
    val groups: List<UiGroup> = emptyList(),
    val tasks: List<DomainTask> = emptyList()
)
