package sample.gthio.tasks.data

import kotlinx.coroutines.test.runTest
import org.junit.Test
import sample.gthio.tasks.data.source.inMemoryGroupSource
import sample.gthio.tasks.domain.model.DomainGroup
import java.util.*

class InMemoryGroupSourceTest {
    @Test
    fun `insertGroup should add a group to the list of groups`() = runTest {
        val source = inMemoryGroupSource()
        val group = DomainGroup(UUID.randomUUID(), "name")
        source.insert(group)
        assert(group.id == source.getById(group.id)?.id)
    }
}