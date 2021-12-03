package com.mazrou.movieApp.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mazrou.movieApp.model.MovieInList


/**
 * @author Mazrou Ayoub
 */


@Database(
    entities = [MovieInList::class],
    version = 4
)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao

    companion object {
        const val DATABASE_NAME = "app_db"

        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }

        }

        private fun buildDatabase(context: Context): AppDataBase {

            return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }


    }
}