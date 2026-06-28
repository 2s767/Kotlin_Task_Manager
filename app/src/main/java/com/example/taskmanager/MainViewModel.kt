package com.example.taskmanager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(
        listOf<Task>(Task("Kotlin o'rganish", true, 1),
            Task("Swift o'rganish", false, 2),
            Task("Rive o'rganish", false, 3),
            Task("Backend ga kirish", false, 4),
            Task("Software ingineer bo'lish", false, 5),
        )
    )

    val tasks : StateFlow<List<Task>> = _tasks

    fun toggleButton(title : String, checked : Boolean) {
        _tasks.value = _tasks.value.map {
            if (it.title == title) it.copy(isDone = checked) else it
        }
    }
}