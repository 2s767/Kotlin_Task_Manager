package com.example.taskmanager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val _tasks = MutableStateFlow<TaskListState>(
        TaskListState.Loading
    )

    val task : StateFlow<TaskListState> = _tasks

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = TaskListState.Loading
            delay(2000) // serverdan yuklashni taqlid qilish uchun
//            _tasks.value = TaskListState.Success(
//                listOf(
//                    Task("Kotlin o'rganish", false, 1),
//                    Task("Swift o'rganish", false, 2),
//                    Task("Software engineer bo'lish", false, 3)
//                )
//            )
            _tasks.value = TaskListState.Error("Xatolik yuz berdi ")
        }
    }

    fun onToggle(isDone : Boolean, task : Task) {
        val current = _tasks.value
        if (current !is TaskListState.Success) return

        _tasks.value = TaskListState.Success(
            current.taskList.map { task1 ->
                if (task1.title == task.title) task1.copy(isDone = isDone) else task1
            }
        )
    }
}
