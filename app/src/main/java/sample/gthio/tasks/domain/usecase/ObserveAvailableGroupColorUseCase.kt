package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.GroupColor
import sample.gthio.tasks.domain.repository.GroupRepository

class ObserveAvailableGroupColorUseCase(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(): Flow<List<GroupColor>> {
        return groupRepository.availableColors
    }
}