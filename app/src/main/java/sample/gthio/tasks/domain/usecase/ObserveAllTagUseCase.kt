package sample.gthio.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow
import sample.gthio.tasks.domain.model.DomainTag
import sample.gthio.tasks.domain.repository.TagRepository

class ObserveAllTagUseCase(
    private val tagRepository: TagRepository
) {
    operator fun invoke(): Flow<List<DomainTag>> {
        return tagRepository.tag
    }
}