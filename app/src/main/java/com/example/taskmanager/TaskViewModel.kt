package com.example.taskmanager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val  _tasks = MutableStateFlow<TaskListState>(TaskListState.LoadingListState)
    val tasks : StateFlow<TaskListState> = _tasks


    init {
        loadStates()
    }
    fun loadStates() {

//        _tasks.value = TaskListState.Error("Yuklashda muammo yuzaga keldi ")

// suspend funksiya bu Flutterda Future funksiyasi bilan bir xil
        // bu holatdagi funksiyalarni faqat suspend funksiyada yoki .launch{} scope larida foydalanish mumkin
        viewModelScope.launch {
            _tasks.value = TaskListState.LoadingListState
            delay(2000)

            _tasks.value = TaskListState.SuccessState(
                data = mutableListOf<TaskModel>(
                    TaskModel("Kotlin o'rganish", true,1),
                    TaskModel("Swift o'rganish", false,2),
                    TaskModel("Backend o'rganish", false,3),
                    TaskModel("Deep Dive kirish C gacha", false,4),
                    TaskModel("SoftWere ingener bo'lish", false,5),
                ))
        }
    }

    fun isComplete(task : TaskModel,isChecked : Boolean){
        val current = _tasks.value

        if(current !is TaskListState.SuccessState) return

        val update = current.data.map { item ->
            if(task.title == item.title ) item.copy(isDone = isChecked) else item
        }
        _tasks.value = TaskListState.SuccessState(data = update)
    }

    fun deleteTask(taskToDelete : TaskModel){
        val current = _tasks.value

        if(current is TaskListState.SuccessState){
           val update = current.data - taskToDelete

            _tasks.value = TaskListState.SuccessState(data = update)

        }
    }

    fun addTask(newTask : String) {
        val current = _tasks.value

        if(current is TaskListState.SuccessState){
            val lastPriority : Int = (current.data.maxOfOrNull { it.priority } ?: 0) + 1
            val update = current.data + TaskModel(newTask,false, lastPriority )
            _tasks.value = TaskListState.SuccessState(data = update)
        }
    }
}