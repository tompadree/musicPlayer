package com.example.musicplayer.di

import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.example.musicplayer.data.source.HomeDataSource
import com.example.musicplayer.data.source.HomeRepository
import com.example.musicplayer.data.source.HomeRepositoryImpl
import com.example.musicplayer.data.source.local.LocalDataSource
import com.example.musicplayer.data.source.local.MusicDatabase
import com.example.musicplayer.data.source.remote.RemoteDataSource
import com.example.musicplayer.ui.home.HomeViewModel
import com.example.musicplayer.utils.helpers.DialogManager
import com.example.musicplayer.utils.helpers.DialogManagerImpl
import com.example.musicplayer.utils.network.InternetConnectionManager
import com.example.musicplayer.utils.network.InternetConnectionManagerImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author Tomislav Curis
 */

val AppModule = module {
    factory { (activity: FragmentActivity) -> DialogManagerImpl(activity) as DialogManager }

    single { InternetConnectionManagerImpl() as InternetConnectionManager }
}

val DataModule = module {

    single { Room.databaseBuilder(androidContext(), MusicDatabase::class.java, "muusic_db").build() }
    single  { get<MusicDatabase>().getMusicDao() }

    single { Dispatchers.IO }

    single(named("local")) { LocalDataSource(get(), get()) as HomeDataSource }
    single(named("remote")) { RemoteDataSource(get()) as HomeDataSource }
}

val RepoModule = module {
    single { HomeRepositoryImpl(get(qualifier = named("local")),
            get(qualifier = named("remote"))) as HomeRepository
    }

    viewModel { HomeViewModel(get(), get()) }
}