package com.example.taskmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskDetailScreen(taskId : String, viewModel: TaskViewModel, onBack : () -> Unit){
    val state = viewModel.tasks.collectAsState()

    val currentState  = state.value

    val task = if(currentState is TaskListState.SuccessState) {
        currentState.data.firstOrNull{it.id == taskId}
    } else null


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextButton(onClick = onBack) {

                Text(text = "Orqaga")
            }

            if(task != null )
            IconButton(onClick = {
                viewModel.deleteTask(taskToDelete = task )
                onBack()
            }
            ) {
                Icon(Icons.Default.Delete, "O'chirish Tugmasi")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if(task == null ){
            Text("Task topilmadi")
        }else {
            Text(text = "Sarlavha : ${task.title}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Bajarilgan : ${if(task.isDone) "Ha" else "Yo'q"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Prioritet : ${task.priority}")
        }
    }
}