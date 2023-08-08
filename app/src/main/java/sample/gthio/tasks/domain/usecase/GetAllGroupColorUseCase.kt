package sample.gthio.tasks.domain.usecase

import sample.gthio.tasks.domain.model.GroupColor
import sample.gthio.tasks.domain.repository.GroupRepository

class GetAllGroupColorUseCase(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(): List<GroupColor> {
        return groupRepository.groupColors
    }
}