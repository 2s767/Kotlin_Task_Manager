package com.example.taskmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                TaskManagerApp()
            }
        }
    }
}


data class Task(val title : String, val isDone : Boolean, val priority : Int)


@Composable
fun TaskManagerApp(viewModel : MainViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            TaskListScreen(
                viewModel = viewModel,
                onTaskClick = { task ->
                    navController.navigate("detail/${task.priority}")
                }
            )
        }

        composable(
            route = "detail/{priority}",
            arguments = listOf(navArgument("priority") { type = NavType.IntType })
        ) { backStackEntry ->
            val priority = backStackEntry.arguments?.getInt("priority") ?: 0
            TaskDetailScreen(
                priority = priority,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


@Composable
fun TaskListScreen(
    viewModel : MainViewModel,
    onTaskClick : (Task) -> Unit
) {
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
                    TaskItem(
                        item = task,
                        onToggle = { isChecked ->
                            viewModel.onToggle(isChecked, task)
                        },
                        onClick = { onTaskClick(task) }
                    )
                }
            }
        }
    }
}


@Composable
fun TaskDetailScreen(
    priority : Int,
    viewModel : MainViewModel,
    onBack : () -> Unit
) {
    val state by viewModel.task.collectAsState()
    val current = state
    if (current !is TaskListState.Success) return

    val task = current.taskList.find { it.priority == priority } ?: return

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = task.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Muhimlik darajasi: ${task.priority}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = if (task.isDone) "Holati: bajarilgan" else "Holati: bajarilmagan")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text(text = "Orqaga")
        }
    }
}


@Composable
fun TaskItem(item : Task, onToggle : (Boolean) -> Unit, onClick : () -> Unit){

    Card (
        modifier =  Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() }
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
