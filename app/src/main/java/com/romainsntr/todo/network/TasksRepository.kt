package com.romainsntr.todo.network

import android.util.Log
import com.romainsntr.todo.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    suspend fun refresh(): List<Task>? {
        val response = tasksWebService.getTasks()
        if (!response.isSuccessful) {
            Log.e("TasksRepository", "Error while fetching tasks: $response")
            return null
        } else {
            Log.e("TasksRepository", "Fetching: ${response.message()}")
        }
        return response.body()
    }

    suspend fun delete(task: Task) : Boolean {
        val response = tasksWebService.delete(task.id)
        if (!response.isSuccessful) {
            Log.e("TasksRepository", "Error while deleting task: $response")
            return false
        } else {
            Log.e("TasksRepository", "Deleting: ${response.message()}")
        }
        return true
    }

    suspend fun create(task: Task) : Task? {
        val response = tasksWebService.create(task)
        if (!response.isSuccessful) {
            Log.e("TasksRepository", "Error while creating task: $response")
            return null
        } else {
            Log.e("TasksRepository", "Creating: ${response.message()}")
        }
        return response.body()
    }

    suspend fun update(task: Task) : Task? {
        val response = tasksWebService.update(task)
        if (!response.isSuccessful) {
            Log.e("TasksRepository", "Error while updating task: $response")
            return null
        } else {
            Log.e("TasksRepository", "Updating: ${response.message()}")
        }
        return response.body()
    }
/*
    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    //private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    //public val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    suspend fun refresh2() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            if (fetchedTasks != null) _taskList.value = fetchedTasks
        }
    }

    suspend fun createOrUpdate2(task: Task) {
        // TODO: appel réseau et récupération de la tache
        val oldTask = taskList.value.firstOrNull { it.id == task.id }
        val response = when {
            oldTask != null -> tasksWebService.update(task)
            else -> tasksWebService.create(task)
        }
        if (response.isSuccessful) {
            val updatedTask = response.body()!!
            if (oldTask != null) _taskList.value = taskList.value - oldTask
            _taskList.value = taskList.value + updatedTask
        }
    }

    suspend fun delete2(task: Task) {

        val response = tasksWebService.delete(task.id)
        if (response.isSuccessful) {
            _taskList.value = _taskList.value - task
        }

    }
 */
}