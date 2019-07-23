package com.demo.ktortodoapi.template

import com.demo.ktortodoapi.data.ToDoDto
import kotlinx.html.*

fun HTML.overviewTemplate(toDos: List<ToDoDto>) {
    head {
        title("Todo Overview")
    }
    body {
        style {
            +"""
                table, th, td {
                    border: 1px solid black;
                    border-collapse: collapse;
                }
                """.trimIndent()
        }
        a {
            href = "/todo/admin/new"
            text("Create new")
        }
        table {
            thead {
                tr {
                    td { +"Name" }
                    td { +"Description" }
                    td { +"Done" }
                }
            }
            toDos.forEach {
                tr {
                    td { +it.name }
                    td { +it.description }
                    td { +it.done.toString() }
                }
            }
        }
    }
}
