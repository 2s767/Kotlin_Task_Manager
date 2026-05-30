package com.example.taskmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
data class Task(val id : String, val title : String, val isDone : Boolean, val priority : Int)
@Composable
fun TaskListScreen(viewModel : TaskViewModel = viewModel())  {
        val state by viewModel.state.collectAsState()

        when(val s = state){
            is TaskListState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                    ){
                    CircularProgressIndicator()
                }

            }
            is TaskListState.Success -> {
                LazyColumn {
                    items(s.tasks){
                        task -> TaskItem(item = task, onToggle = {
                            checked -> viewModel.toggleTask(task.id,checked)
                        })
                    }
                }
            }
            is TaskListState.Error -> {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                    ){
                    Text(text = "Xato : ${s.message}")
                }
            }
        }

}
@Composable
fun TaskItem(item : Task,onToggle  : (Boolean) -> Unit){

    Card (
        modifier =  Modifier.fillMaxWidth().padding(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.title)
            Checkbox(checked = item.isDone, onCheckedChange = onToggle
            )
        }
    }
}