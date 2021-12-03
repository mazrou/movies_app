package com.mazrou.movieApp

import android.app.Application
import com.mazrou.movieApp.network.WebService
import com.mazrou.movieApp.persistance.AppDataBase
import com.mazrou.movieApp.repository.main.Repository
import com.mazrou.movieApp.repository.main.RepositoryImpl
import com.mazrou.movieApp.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
class BaseApplication : Application(), KodeinAware {



    override val kodein: Kodein by Kodein.lazy {

        import(androidXModule(this@BaseApplication))

        // data base instance
        bind<AppDataBase>() with singleton { AppDataBase.invoke(instance()) }

        // web service instance
        bind<WebService>() with  singleton { WebService.invoke() }

        // repository
        bind <Repository>() with singleton { RepositoryImpl(
            webService = instance() ,
            dao = instance<AppDataBase>().getMoviesDao()
            )
        }

        // viewModel
        bind<MainViewModel>() with singleton { MainViewModel(instance()) }
    }
}