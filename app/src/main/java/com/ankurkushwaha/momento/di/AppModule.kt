package com.ankurkushwaha.momento.di

import androidx.room.Room
import androidx.work.WorkManager
import com.ankurkushwaha.momento.data.local.db.MomentoDatabase
import com.ankurkushwaha.momento.data.repository.NotesRepositoryImpl
import com.ankurkushwaha.momento.data.repository.TaskRepositoryImpl
import com.ankurkushwaha.momento.domain.repository.NotesRepository
import com.ankurkushwaha.momento.domain.repository.TaskRepository
import com.ankurkushwaha.momento.presentation.notes_screen.NotesViewModel
import com.ankurkushwaha.momento.presentation.todos_screen.TaskAlertWorkerViewModel
import com.ankurkushwaha.momento.presentation.todos_screen.TaskViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:57
 */

val appModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            MomentoDatabase::class.java,
            "momento_db.db"
        ).build()
    }

    single { get<MomentoDatabase>().notesDao() }

    single { get<MomentoDatabase>().taskDao() }

//    singleOf(::NotesRepositoryImpl).bind<NotesRepository>()
    single<NotesRepository> { NotesRepositoryImpl(get()) }

    single<TaskRepository> { TaskRepositoryImpl(get()) }
//    singleOf(::TaskRepositoryImpl).bind<TaskRepository>()

    // Provide WorkManager instance
    single { WorkManager.getInstance(get()) }

    // ViewModel
    viewModel { NotesViewModel(get()) }

    viewModel { TaskViewModel(get()) }

    viewModel{ TaskAlertWorkerViewModel(get()) }
}