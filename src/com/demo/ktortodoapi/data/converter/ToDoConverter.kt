package com.demo.ktortodoapi.data.converter

import com.demo.ktortodoapi.data.ToDoDto
import com.demo.ktortodoapi.data.entity.ToDo
import org.jetbrains.exposed.sql.ResultRow

object ToDoConverter {

    fun convertResultToDto(entity: ResultRow) =
        ToDoDto(
            id = entity[ToDo.id],
            name = entity[ToDo.name],
            description = entity[ToDo.description],
            done = entity[ToDo.done]
        )
}