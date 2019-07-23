package com.demo.ktortodoapi.data

data class ToDoDto(var name: String, var description: String, var done: Boolean = false, var id: Long? = null)