package com.example.taskmanager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val _state = MutableStateFlow<TaskListState>(
        TaskListState.Loading
    )
    val state : StateFlow<TaskListState>  = _state

    init {
        loadTask()
    }

    private fun loadTask (){
        viewModelScope.launch {
            _state.value = TaskListState.Loading
            delay(2000)
//            _state.value = TaskListState.Error("Internet bilan ulanish yo'q")
            _state.value = TaskListState.Success(
               tasks =  listOf(Task( "1","Kotlin o'rganish", true, 1),
                    Task("2","Swift o'rganish", false, 2),
                    Task("3","Rive o'rganish", false, 3),
                    Task("4","Backend ga kirish", false, 4),
                    Task("5","Software ingineer bo'lish", false, 5),
                )
            )
        }
    }


    fun toggleTask(id: String, checked : Boolean){
        val current = _state.value

        if(current is TaskListState.Success){
            _state.value = current.copy(
                tasks = current.tasks.map {
                    if(it.id == id) it.copy(isDone = checked) else it
                }
            )
        }
    }
}