package sample.gthio.tasks.domain.usecase

import sample.gthio.tasks.domain.repository.GroupRepository
import java.util.UUID
import javax.inject.Inject

class DeleteGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupId: UUID) {
        groupRepository.deleteGroupById(groupId)
    }
}