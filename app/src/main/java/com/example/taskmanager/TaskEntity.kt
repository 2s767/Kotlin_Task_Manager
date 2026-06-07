package com.example.taskmanager

import androidx.compose.foundation.MutatePriority
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id : String,
    val title : String,
    val isDone : Boolean,
    val priority: Int
)