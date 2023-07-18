package sample.gthio.tasks.ui.route

import sample.gthio.tasks.domain.model.DomainTag

sealed interface HomeEvent {

    object SelectAllTags: HomeEvent
    data class SelectTag(val tag: DomainTag): HomeEvent

}