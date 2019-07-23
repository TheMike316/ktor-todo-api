package com.demo.ktortodoapi.data.entity

import org.jetbrains.exposed.sql.Table

object ToDo : Table() {
    val id = long("id").primaryKey().autoIncrement()
    val name = text("name")
    val description = text("description")
    val done = bool("done")
}