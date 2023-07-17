package sample.gthio.tasks.ui.route

import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.model.DomainTask

data class SampleRouteState(
    val tags: List<DomainTag> = emptyList(),
    val selectedTag: DomainTag? = null,
    val tasks: List<DomainTask> = emptyList(),
) {
    val selectedTagTasks: List<DomainTask>
        get() = tasks.filter { task ->
            task
                .tags
                .map { tag -> tag.id }
                .contains(selectedTag?.id)
        }
}
