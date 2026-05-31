package com.example.taskmanager
import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.ui.theme.TaskManagerTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                Scaffold(
                    topBar = {AppBar()}
                ) {
                    innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        TaskListScreen()
                    }
                }
            }
        }
    }
}

data class TaskModel(val title : String, val isDone : Boolean, val priority : Int)

@Composable
fun TaskListScreen(viewModel: TaskViewModel = viewModel()) {
    val tasks = viewModel.tasks.collectAsState() // StateFlow ni Compose state ga aylantiradi . Kuzatishni boshlash uchun

    when(val s = tasks.value){
        is TaskListState.LoadingListState -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is TaskListState.SuccessState -> {

            LazyColumn(Modifier.fillMaxSize()) {
                items(s.data){
                        task -> Task(task, onPressed = { isChecked -> viewModel.isComplete(task,isChecked)
                } , onPressToDelete = { viewModel.deleteTask(taskToDelete = task) })
                }
            }
        }

       is TaskListState.Error -> {
           Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
               Text(text = s.message)
           }

       }
    }
}
@Composable
fun Task( task : TaskModel, onPressed : (Boolean) -> Unit, onPressToDelete : () -> Unit){


    Box(modifier = Modifier.fillMaxSize().padding(12.dp)){
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Text(task.title)
            Spacer(modifier = Modifier.padding(horizontal = 20.dp))
            IconButton(onClick = onPressToDelete, ) {
                Icon(Icons.Filled.Delete,"Delete Button")
            }
            Checkbox(checked = task.isDone, onCheckedChange = onPressed)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(viewModel: TaskViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    TopAppBar(
        title = { Text(text = "TODO APP") },
        actions = {
            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(Icons.Outlined.Add,"Plus button")
            }
        },
        navigationIcon = {

        }
    )
    if(showDialog) {
        BasicAlertDialog({
        showDialog = false
        }, Modifier, DialogProperties(), {
            Column() {
                OutlinedTextField(
                    value= text,
                    onValueChange = {text = it},
                    label = {Text(text = "Vaziyfa nomini kiriting", color = Color.White, fontSize = 18.sp) },
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
                )
                Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {

                    TextButton(onClick = {
                        if(text.isNotEmpty()) viewModel.addTask(text)
                        showDialog = false
                    }) { Text(text = "Add") }
                }
            }
        })
    }
}