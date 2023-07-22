package sample.gthio.tasks.ui.route.home

import sample.gthio.tasks.domain.model.DomainTag

sealed interface HomeEvent {

    object SelectAllTags: HomeEvent
    data class SelectTag(val tag: DomainTag): HomeEvent
    object FabClick: HomeEvent
    object AddClick: HomeEvent

}