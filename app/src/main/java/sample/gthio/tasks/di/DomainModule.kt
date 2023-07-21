package sample.gthio.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sample.gthio.tasks.data.repository.defaultGroupRepository
import sample.gthio.tasks.data.repository.defaultTagRepository
import sample.gthio.tasks.data.repository.defaultTaskRepository
import sample.gthio.tasks.data.source.GroupLocalSource
import sample.gthio.tasks.data.source.TagLocalSource
import sample.gthio.tasks.data.source.TaskLocalSource
import sample.gthio.tasks.domain.repository.GroupRepository
import sample.gthio.tasks.domain.repository.TagRepository
import sample.gthio.tasks.domain.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideTaskRepository(
        taskLocalSource: TaskLocalSource
    ): TaskRepository =
        defaultTaskRepository(taskLocalSource)

    @Singleton
    @Provides
    fun provideTagRepository(
        tagLocalSource: TagLocalSource
    ): TagRepository =
        defaultTagRepository(tagLocalSource)

    @Singleton
    @Provides
    fun provideGroupRepository(
        groupLocalSource: GroupLocalSource
    ): GroupRepository =
        defaultGroupRepository(groupLocalSource)
}