package sample.gthio.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import sample.gthio.tasks.domain.repository.GroupRepository
import sample.gthio.tasks.domain.repository.TagRepository
import sample.gthio.tasks.domain.repository.TaskRepository
import sample.gthio.tasks.domain.usecase.*

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideUpsertGroup(
        groupRepository: GroupRepository,
        taskRepository: TaskRepository
    ): UpsertGroupUseCase =
        UpsertGroupUseCase(groupRepository, taskRepository)

    @Provides
    fun provideUpsertTag(
        tagRepository: TagRepository,
        taskRepository: TaskRepository
    ): UpsertTagUseCase =
        UpsertTagUseCase(taskRepository, tagRepository)

    @Provides
    fun provideUpsertTask(
        taskRepository: TaskRepository
    ): UpsertTaskUseCase =
        UpsertTaskUseCase(taskRepository)

    @Provides
    fun provideGetAllGroup(
        groupRepository: GroupRepository
    ): ObserveAllGroupUseCase =
        ObserveAllGroupUseCase(groupRepository)

    @Provides
    fun provideGetAllTag(
        tagRepository: TagRepository
    ): ObserveAllTagUseCase =
        ObserveAllTagUseCase(tagRepository)

    @Provides
    fun provideGetAllTask(
        taskRepository: TaskRepository
    ): ObserveAllTaskUseCase =
        ObserveAllTaskUseCase(taskRepository)

    @Provides
    fun provideGetTaskByTag(
        taskRepository: TaskRepository
    ): ObserveTaskByTagUseCase =
        ObserveTaskByTagUseCase(taskRepository)

    @Provides
    fun provideGetTaskByGroup(
        taskRepository: TaskRepository
    ): ObserveAllTaskByGroupUseCase = ObserveAllTaskByGroupUseCase(taskRepository)

    @Provides
    fun provideGetTaskByGroupAndTag(
        taskRepository: TaskRepository
    ): ObserveAllTaskByGroupAndTagUseCase =
        ObserveAllTaskByGroupAndTagUseCase(taskRepository)

    @Provides
    fun provideGetTaskByQueries(
        taskRepository: TaskRepository
    ): ObserveTaskByQueriesUseCase =
        ObserveTaskByQueriesUseCase(taskRepository)

    @Provides
    fun provideGetTaskWithQuery(
        taskRepository: TaskRepository
    ): ObserveTaskWithQuery =
        ObserveTaskWithQuery(taskRepository)

    @Provides
    fun provideObserveTaskById(
        taskRepository: TaskRepository
    ): ObserveTaskUseCase =
        ObserveTaskUseCase(taskRepository)

    @Provides
    fun provideGetAllGroupColor(
        groupRepository: GroupRepository
    ): GetAllGroupColorUseCase =
        GetAllGroupColorUseCase(groupRepository)

    @Provides
    fun provideObserveAvailableGroupColor(
        groupRepository: GroupRepository
    ): ObserveAvailableGroupColorUseCase =
        ObserveAvailableGroupColorUseCase(groupRepository)
}