package sample.gthio.tasks.ui.route.addtask

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag

sealed interface AddTaskEvent {
    object BackPressed: AddTaskEvent
    data class TitleValueChange(val value: String): AddTaskEvent
    data class DescriptionValueChange(val value: String): AddTaskEvent
    object ImportantSelect: AddTaskEvent
    data class TagSelect(val tag: DomainTag): AddTaskEvent
    data class NewTagValueChange(val newTag: String): AddTaskEvent
    data class NewTagAddButtonClick(val newTag: String): AddTaskEvent
    data class GroupSelect(val group: DomainGroup): AddTaskEvent
}