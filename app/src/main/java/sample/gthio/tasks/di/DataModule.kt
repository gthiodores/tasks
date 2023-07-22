package sample.gthio.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sample.gthio.tasks.data.source.GroupLocalSource
import sample.gthio.tasks.data.source.TagLocalSource
import sample.gthio.tasks.data.source.TaskLocalSource
import sample.gthio.tasks.data.source.inMemoryGroupSource
import sample.gthio.tasks.data.source.inMemoryTagSource
import sample.gthio.tasks.data.source.inMemoryTaskSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideTaskLocalSource(): TaskLocalSource = inMemoryTaskSource()

    @Singleton
    @Provides
    fun provideTagLocalSource(): TagLocalSource = inMemoryTagSource()

    @Singleton
    @Provides
    fun provideGroupLocalSource(): GroupLocalSource = inMemoryGroupSource()

}