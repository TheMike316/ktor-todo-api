package com.demo.ktortodoapi.service

import com.demo.ktortodoapi.data.ToDoDto
import com.demo.ktortodoapi.data.ToDoPatchDto
import com.demo.ktortodoapi.data.converter.ToDoConverter
import com.demo.ktortodoapi.data.entity.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

class ToDoService(dataSource: DataSource) {

    init {
        Database.connect(dataSource)
        transaction {
            create(ToDo)
        }
    }

    suspend fun findAll(): List<ToDoDto> =
        dbQuery {
            ToDo.selectAll()
                .map { ToDoConverter.convertResultToDto(it) }
        }

    suspend fun findById(id: Long): ToDoDto? =
        dbQuery {
            ToDo.select {
                ToDo.id eq id
            }.mapNotNull { ToDoConverter.convertResultToDto(it) }
                .singleOrNull()
        }

    suspend fun create(toDoDto: ToDoDto): ToDoDto =
        dbQuery {
            ToDo.insert {
                it[name] = toDoDto.name ?: ""
                it[description] = toDoDto.description ?: ""
                it[done] = toDoDto.done ?: false
            }.resultedValues?.map { ToDoConverter.convertResultToDto(it) }
                ?.single() ?: throw RuntimeException("Something went terribly wrong")
        }

    suspend fun update(toDoDto: ToDoDto, id: Long): ToDoDto =
        dbQuery {
            ToDo.select {
                ToDo.id eq id
            }.singleOrNull() ?: throw RuntimeException("ToDo Item not found!")
            ToDo.update({ ToDo.id eq id }) {
                it[this.id] = id
                it[name] = toDoDto.name ?: ""
                it[description] = toDoDto.description ?: ""
                it[done] = toDoDto.done ?: false
            }
            ToDo.select {
                ToDo.id eq id
            }.mapNotNull { ToDoConverter.convertResultToDto(it) }
                .singleOrNull() ?: throw IllegalStateException("Something went horribly wrong")
        }

    suspend fun patch(toDoDto: ToDoPatchDto, id: Long): ToDoDto =
        dbQuery {
            val persistedEntity = ToDo.select {
                ToDo.id eq id
            }.singleOrNull() ?: throw RuntimeException("ToDo Item not found!")
            ToDo.update({ ToDo.id eq id }) {
                toDoDto.name?.let { dtoName -> if (persistedEntity[name] != dtoName) it[name] = dtoName }
                toDoDto.description?.let { dtoDesc ->
                    if (persistedEntity[description] != dtoDesc) it[description] = dtoDesc
                }
                toDoDto.done?.let { dtoDone -> if (persistedEntity[done] != dtoDone) it[done] = dtoDone }
            }
            ToDo.select {
                ToDo.id eq id
            }.mapNotNull { ToDoConverter.convertResultToDto(it) }
                .singleOrNull() ?: throw IllegalStateException("Something went horribly wrong")
        }

    suspend fun deleteById(id: Long) {
        dbQuery {
            ToDo.deleteWhere {
                ToDo.id eq id
            }
        }
    }

    private suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}