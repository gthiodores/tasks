package sample.gthio.tasks.ui.model

import sample.gthio.tasks.domain.model.DomainGroup

data class UiGroup(
    val group: DomainGroup,
    val quantity: Int,
)