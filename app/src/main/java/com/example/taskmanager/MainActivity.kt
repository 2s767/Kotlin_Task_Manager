package com.example.taskmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                TaskListScreen(

                )
            }
        }
    }
}
data class Task(val title : String, val isDone : Boolean, val priority : Int)
@Composable
fun TaskListScreen() {
    val tasks = listOf<Task>(Task("Kotlin o'rganish", true, 1),
        Task("Swift o'rganish", false, 2),
        Task("Rive o'rganish", false, 3),
        Task("Backend ga kirish", false, 4),
        Task("Software ingineer bo'lish", false, 5),
        )
    LazyColumn {
        items(tasks){ task ->
            TaskItem(task)
        }
    }
}
@Composable
fun TaskItem(item : Task){
    var isDone by remember { mutableStateOf(item.isDone) }
    Card (
        modifier =  Modifier.fillMaxWidth().padding(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.title, modifier = Modifier.padding(16.dp).background(Color.Yellow))
            Checkbox(checked = isDone, onCheckedChange = {
                isDone = it
            })
        }
    }
}