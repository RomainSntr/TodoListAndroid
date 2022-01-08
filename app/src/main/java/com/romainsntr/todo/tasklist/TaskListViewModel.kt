package com.romainsntr.todo.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romainsntr.todo.network.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    public var taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            val refreshList = repository.refresh()
            if (refreshList != null) {
                _taskList.value = refreshList
            }
        }
    }
    fun delete(task: Task) {
        viewModelScope.launch {
            if (repository.delete(task)) {
                _taskList.value = _taskList.value - task
            }
        }
    }
    fun addOrEdit(task: Task) {

        viewModelScope.launch {
            val oldTask = _taskList.value.firstOrNull { it.id == task.id }
            var newTask: Task?
            if (oldTask != null) {
                newTask = repository.update(task)
                if (newTask != null) {
                    _taskList.value = _taskList.value - oldTask + task
                }
            }
            else {
                newTask = repository.create(task)
                if (newTask != null) {
                    _taskList.value = _taskList.value + task
                }
            }
        }

    }
}