package com.romainsntr.todo.network

import com.romainsntr.todo.tasklist.Task
import retrofit2.Response
import retrofit2.http.GET

interface TaskWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>
}