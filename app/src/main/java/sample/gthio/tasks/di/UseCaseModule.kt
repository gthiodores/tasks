package sample.gthio.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sample.gthio.tasks.domain.repository.GroupRepository
import sample.gthio.tasks.domain.repository.TagRepository
import sample.gthio.tasks.domain.repository.TaskRepository
import sample.gthio.tasks.domain.usecase.ObserveAllGroupUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTagUseCase
import sample.gthio.tasks.domain.usecase.ObserveAllTaskUseCase
import sample.gthio.tasks.domain.usecase.ObserveTaskByTagUseCase
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
    ): ObserveAllGroupUseCase =
        ObserveAllGroupUseCase(groupRepository)

    @Singleton
    @Provides
    fun provideGetAllTag(
        tagRepository: TagRepository
    ): ObserveAllTagUseCase =
        ObserveAllTagUseCase(tagRepository)

    @Singleton
    @Provides
    fun provideGetAllTask(
        taskRepository: TaskRepository
    ): ObserveAllTaskUseCase =
        ObserveAllTaskUseCase(taskRepository)

    @Singleton
    @Provides
    fun provideGetTaskByTag(
        taskRepository: TaskRepository
    ): ObserveTaskByTagUseCase =
        ObserveTaskByTagUseCase(taskRepository)
}