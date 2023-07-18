package sample.gthio.tasks.ui.route

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
    private val selectedTagTasks: List<DomainTask>
        get() = tasks.filter { task ->
            task
                .tags
                .map { tag -> tag.id }
                .contains(selectedTag?.id)
        }

    val uiGroups: List<UiGroup> = groups
        .map { group ->
            UiGroup(
                group = group,
                quantity =
                    if (selectedTag != null) {
                        selectedTagTasks.count { it.group == group }
                    } else {
                        tasks.count { it.group == group }
                    }
            )
        }
}