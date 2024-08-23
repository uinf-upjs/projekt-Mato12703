package com.example.upjsfd


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.upjsfd.dao.FilmDao
import com.example.upjsfd.dao.ReviewDao
import com.example.upjsfd.dao.UserDao
import com.example.upjsfd.entities.Film
import com.example.upjsfd.entities.Review
import com.example.upjsfd.entities.User

@Database(entities = [User::class, Film::class, Review::class], version = 3, exportSchema = false)
abstract class UPJSFdDatabase : RoomDatabase() {
    abstract fun filmDao():FilmDao
    abstract fun reviewDao(): ReviewDao
    abstract fun userDao(): UserDao
    companion object{
        @Volatile
        private var INSTANCE: UPJSFdDatabase? = null

        fun getDatabase(context: Context):UPJSFdDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UPJSFdDatabase::class.java,
                    "upjsfd_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}