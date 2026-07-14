package com.example.taskmanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao

    companion object{  // class ga bog'liq static a'zolar
        @Volatile // Threadlar orasida ko'rinarli qilish uchun
        private var INSTANCE : TaskDatabase? = null

        fun getInstance(context : Context) : TaskDatabase{
            return INSTANCE ?: synchronized(this){ // synchronized - bu bir vaqtda faqat bitta thread uchun ruxsat borligini bildiradi.
                val instance = Room.databaseBuilder(  // Room ning database yaratadigan factory methodi
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}