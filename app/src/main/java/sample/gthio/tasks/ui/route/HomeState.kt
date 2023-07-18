package sample.gthio.tasks.ui.route

import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.ui.model.UiGroup

data class HomeState(
    val tags: List<DomainTag> = emptyList(),
    val selectedTag: DomainTag? = null,
    val groups: List<UiGroup> = emptyList(),
) {
//    val selectedTagTasks: List<DomainTask>
//        get() = tasks.filter { task ->
//            task
//                .tags
//                .map { tag -> tag.id }
//                .contains(selectedTag?.id)
//        }
}
