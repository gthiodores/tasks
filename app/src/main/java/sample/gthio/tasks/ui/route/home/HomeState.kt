package sample.gthio.tasks.ui.route.home

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.model.DomainTask
import sample.gthio.tasks.ui.model.UiGroup

data class HomeState(
    val tags: List<DomainTag> = emptyList(),
    val selectedTag: DomainTag? = null,
    val groups: List<DomainGroup> = emptyList(),
    val tasks: List<DomainTask> = emptyList()
) {
    val uiGroups: List<UiGroup> = groups
        .map { group ->
            UiGroup(
                group = group,
                quantity = tasks.count { it.group == group }
            )
        }
}
