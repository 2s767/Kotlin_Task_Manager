package com.example.taskmanager

import kotlinx.coroutines.flow.StateFlow

sealed class TaskListState {
    object LoadingListState : TaskListState()
    data class SuccessState(val data : List<TaskModel>, val user : User ) : TaskListState()
    data class Error(val message : String) : TaskListState()
}