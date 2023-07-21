package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainGroup
import sample.gthio.tasks.domain.repository.GroupRepository

class GetAllGroupsUseCase(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(): Flow<List<DomainGroup>> {
        return groupRepository.groups
    }
}