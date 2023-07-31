package sample.gthio.tasks.ui.route.home

import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.model.DomainTag

sealed interface HomeEvent {
    object SelectAllTags: HomeEvent
    data class SelectTag(val tag: DomainTag): HomeEvent
    object FabClick: HomeEvent
    object AddClick: HomeEvent
    object AllTasksClick: HomeEvent
    object ImportantClick: HomeEvent
    object TodayClick: HomeEvent
    data class GroupItemClick(val group: DomainGroup): HomeEvent
}