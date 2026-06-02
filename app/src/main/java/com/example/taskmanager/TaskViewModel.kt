package com.example.taskmanager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class TaskViewModel : ViewModel() {
    private val  _tasks = MutableStateFlow<TaskListState>(TaskListState.LoadingListState)
    val tasks : StateFlow<TaskListState> = _tasks

    init {
        loadStates()
    }
    private suspend fun fetchTasksFromServer() : List<TaskModel> {
        delay(2000)
        if(Random.nextBoolean()) throw Exception("Network Error")
        return listOf<TaskModel>(
            TaskModel( title = "Kotlin o'rganish", isDone =  true, priority = 1),
            TaskModel(title = "Swift o'rganish", isDone =  false, priority = 2),
            TaskModel(title = "Backend o'rganish", isDone =  false, priority = 3),
            TaskModel(title = "Deep Dive kirish C gacha", isDone = false, priority = 4),
            TaskModel(title = "SoftWere ingener bo'lish", isDone =  false, priority = 5),
        )
    }

    private suspend fun fetchUserProfile() : User {
        delay(1500)
        if(Random.nextBoolean()) throw Exception("User profile yuklanmadi")
        return User("Asror", "yodgorovasror37@gmail.com")
    }
    fun loadStates() {

//  _tasks.value = TaskListState.Error("Yuklashda muammo yuzaga keldi ")

// suspend funksiya bu Flutterda Future funksiyasi bilan bir xil
        // bu holatdagi funksiyalarni faqat suspend funksiyada yoki .launch{} scope larida foydalanish mumkin
        viewModelScope.launch {
            val start = System.currentTimeMillis()
            _tasks.value = TaskListState.LoadingListState
            try {

//                supervisorScope { } Bu Bitta bola Crash bo'lgan taqdirda boshqalari ishlashda davom etsin degan maqsadda ishlatilinadi .

                // E.x : supervisorScope {
                //    val tasksDeferred = async {
                //        try { fetchTasksFromServer() }
                //        catch (e: Exception) { null }                    // xato bo'lsa null
                //    }
                //    val userDeferred = async {
                //        try { fetchUserProfile() }
                //        catch (e: Exception) { User("Guest", "") }       // xato bo'lsa default
                //    }
                //
                //    val tasks = tasksDeferred.await() ?: emptyList()
                //    val user = userDeferred.await()
                //    // ...
                //}

                val (tasks, user ) = coroutineScope { // Bu Exception otilganda viewModelScope ni vayron qilmaslik uchun foydalanildi ya'ni Exception otilganda coroutine tutib olib vayron bo'ladi lekin viewModelga o'tmaydi
                    // Bunda Falsafa shuki bir bola vayron bo'lsa, qolgan barcha courotine funksiyalar ishi to'xtaydi .
                    val tasksDeferred  =  async {fetchTasksFromServer()}
                    val userDeferred = async { fetchUserProfile() }
                    Pair(tasksDeferred.await(), userDeferred.await()) // Bu 2 ta taskni birdan qiymatini olish uchun foydalanildi
                }

                val duration = System.currentTimeMillis() - start

                println("Yuklanish vaqti : ${duration}ms")
                _tasks.value = TaskListState.SuccessState(user = user, data = tasks)

            }catch (e : Exception){
                _tasks.value = TaskListState.Error(message = e.message ?: "Noma'lum Xato !")
            }

        }
    }

    fun isComplete(task : TaskModel,isChecked : Boolean){
        val current = _tasks.value

        if(current !is TaskListState.SuccessState) return

       _tasks.value = current.copy(
           data = current.data.map { item ->
               if(task.id == item.id ) item.copy(isDone = isChecked) else item
           }
       )
    }

    fun deleteTask(taskToDelete : TaskModel){
        val current = _tasks.value

        if (current !is TaskListState.SuccessState ) return
           val update = current.data - taskToDelete

        _tasks.value = current.copy(data = update)

        }


    fun addTask(newTask : String) {
        val current = _tasks.value

        if(current !is TaskListState.SuccessState) return
            val lastPriority : Int = (current.data.maxOfOrNull { it.priority } ?: 0) + 1
            val update = current.data + TaskModel(title = newTask, isDone = false, priority =  lastPriority )
            _tasks.value = current.copy(data = update)

    }
}

//Bu — Structured Concurrency falsafasi: "agar bir narsa muvaffaqiyatsiz bo'lsa, qolganlari kerakmas — bekor qilamiz".

//Flutter'da bu yo'q. Dart'da Future.wait([f1, f2]) ichida bittasi xato bersa — boshqasi davom etadi.