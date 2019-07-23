package com.demo.ktortodoapi.controller

import com.demo.ktortodoapi.data.ToDoDto
import com.demo.ktortodoapi.data.ToDoPatchDto
import com.demo.ktortodoapi.service.ToDoService
import com.demo.ktortodoapi.template.overviewTemplate
import com.demo.ktortodoapi.template.toDoFormTemplate
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.*

fun Routing.toDo(toDoService: ToDoService) =
    route("/todo") {
        get {
            call.respond(toDoService.findAll())
        }
        get("/{id}") {
            call.parameters["id"]?.toLongOrNull()
                ?.also { call.respond(toDoService.findById(it) ?: throw RuntimeException("not found")) }
        }
        post {
            call.receive<ToDoDto>().let { toDoService.create(it) }
                .also { call.respond(HttpStatusCode.Created, it) }
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid id")
            call.receive<ToDoDto>().let { toDoService.update(it, id) }
                .also { call.respond(it) }
        }
        patch("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid id")
            call.receive<ToDoPatchDto>().let { toDoService.patch(it, id) }
                .also { call.respond(it) }
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid id")
            toDoService.deleteById(id)
            call.respond("Successfully deleted")
        }
        route("/admin") {
            authenticate("myBasicAuth") {
                get {
                    val all = toDoService.findAll()
                    call.respondHtml { overviewTemplate(all) }
                }
                get("/new") {
                    call.respondHtml { toDoFormTemplate() }
                }
                post("/new") {
                    val data = call.receiveParameters().let {
                        ToDoDto(
                            name = it["name"]!!,
                            description = it["description"]!!,
                            done = it["done"]?.let { true } ?: false
                        )
                    }
                    toDoService.create(data)
                    call.respondRedirect("/todo/admin")
                }
            }
        }
    }