package com.romainsntr.todo.tasklist

import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: String, var title: String, var description: String = "test") : java.io.Serializable {

}