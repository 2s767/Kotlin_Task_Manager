package com.example.taskmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


data class Task(val title : String, val isDone : Boolean, val priority : Int)


@Composable
fun TaskListScreen(viewModel : MainViewModel = viewModel()) {
    val state by viewModel.task.collectAsState()

    when (val current = state) {
        is TaskListState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is TaskListState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = current.message, color = Color.Red)
            }
        }

        is TaskListState.Success -> {
            LazyColumn {
                items(current.taskList){ task ->
                    TaskItem(task){ isChecked ->
                        viewModel.onToggle(isChecked,task)
                    }
                }
            }
        }
    }
}


@Composable
fun TaskItem(item : Task, onToggle : (Boolean) -> Unit){

    Card (
        modifier =  Modifier.fillMaxWidth().padding(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.title, modifier = Modifier.padding(16.dp).background(Color.Yellow))
            Checkbox(checked = item.isDone, onCheckedChange = onToggle)
            IconButton(onClick = {

            }) {  }
        }
    }
}
