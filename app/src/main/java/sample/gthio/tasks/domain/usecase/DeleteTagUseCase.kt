package sample.gthio.tasks.domain.usecase

import sample.gthio.tasks.domain.repository.TagRepository
import java.util.UUID
import javax.inject.Inject

class DeleteTagUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(tagId: UUID) {
        tagRepository.deleteTagById(tagId)
    }
}