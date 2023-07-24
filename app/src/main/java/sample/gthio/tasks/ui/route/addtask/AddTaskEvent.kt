package sample.gthio.tasks.ui.route.addtask

import sample.gthio.tasks.domain.model.DomainGroup

sealed interface AddTaskEvent {
    object BackPressed: AddTaskEvent
    data class TitleValueChange(val value: String): AddTaskEvent
    data class DescriptionValueChange(val value: String): AddTaskEvent
    object ImportantSelect: AddTaskEvent
    data class GroupSelect(val group: DomainGroup): AddTaskEvent
}