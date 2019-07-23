package com.demo.ktortodoapi.template

import com.demo.ktortodoapi.data.ToDoDto
import kotlinx.html.*

fun HTML.toDoFormTemplate(data: ToDoDto? = null) {
    head {
        title("Todo Form")
    }
    body {
        form(method = FormMethod.post, action = "/todo/admin/new") {
            data?.let {
                input(type = InputType.hidden, name = "id") {
                    data.id
                }
            }
            label { +"Name" }
            input(type = InputType.text, name = "name") {
                data?.let { text(it.name) }
            }
            br()
            label { +"Description" }
            input(type = InputType.text, name = "description") {
                data?.let { text(it.description) }
            }
            br()
            label { +"Done" }
            checkBoxInput(name = "done") {
                data?.run { checked = done }
            }
            br()
            input(type = InputType.submit) {
                +"Erstellen"
            }
        }
    }
}