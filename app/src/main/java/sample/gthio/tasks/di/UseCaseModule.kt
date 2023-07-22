package sample.gthio.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sample.gthio.tasks.domain.repository.GroupRepository
import sample.gthio.tasks.domain.repository.TagRepository
import sample.gthio.tasks.domain.repository.TaskRepository
import sample.gthio.tasks.domain.usecase.GetAllGroupsUseCase
import sample.gthio.tasks.domain.usecase.GetAllTagsUseCase
import sample.gthio.tasks.domain.usecase.GetAllTasksUseCase
import sample.gthio.tasks.domain.usecase.GetTaskByTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertGroupUseCase
import sample.gthio.tasks.domain.usecase.UpsertTagUseCase
import sample.gthio.tasks.domain.usecase.UpsertTaskUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideUpsertGroup(
        groupRepository: GroupRepository,
        taskRepository: TaskRepository
    ): UpsertGroupUseCase =
        UpsertGroupUseCase(groupRepository, taskRepository)

    @Singleton
    @Provides
    fun provideUpsertTag(
        tagRepository: TagRepository,
        taskRepository: TaskRepository
    ): UpsertTagUseCase =
        UpsertTagUseCase(taskRepository, tagRepository)

    @Singleton
    @Provides
    fun provideUpsertTask(
        taskRepository: TaskRepository
    ): UpsertTaskUseCase =
        UpsertTaskUseCase(taskRepository)

    @Singleton
    @Provides
    fun provideGetAllGroup(
        groupRepository: GroupRepository
    ): GetAllGroupsUseCase =
        GetAllGroupsUseCase(groupRepository)

    @Singleton
    @Provides
    fun provideGetAllTag(
        tagRepository: TagRepository
    ): GetAllTagsUseCase =
        GetAllTagsUseCase(tagRepository)

    @Singleton
    @Provides
    fun provideGetAllTask(
        taskRepository: TaskRepository
    ): GetAllTasksUseCase =
        GetAllTasksUseCase(taskRepository)

    @Singleton
    @Provides
    fun provideGetTaskByTag(
        taskRepository: TaskRepository
    ): GetTaskByTagUseCase =
        GetTaskByTagUseCase(taskRepository)
}