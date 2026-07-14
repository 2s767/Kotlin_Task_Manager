package com.example.taskmanager

sealed class TaskListState{
    object Loading : TaskListState()
    data class Success(val taskList : List<Task>) : TaskListState()
    data class Error(val message : String) : TaskListState()
}