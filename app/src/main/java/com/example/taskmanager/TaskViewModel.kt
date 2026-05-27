package com.example.taskmanager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(
        listOf(Task("Kotlin o'rganish", true, 1),
            Task("Swift o'rganish", false, 2),
            Task("Rive o'rganish", false, 3),
            Task("Backend ga kirish", false, 4),
            Task("Software ingineer bo'lish", false, 5),
        )
    )

    val tasks : StateFlow<List<Task>>  = _tasks

    fun toggleTask(title: String, checked : Boolean){
        _tasks.value = _tasks.value.map {
            if(title == it.title) it.copy(isDone = checked) else it
        }
    }
}