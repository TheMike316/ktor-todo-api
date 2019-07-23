package com.demo.ktortodoapi.config

import com.demo.ktortodoapi.service.ToDoService
import org.koin.dsl.module

fun declareBeans() = module {
    single { ToDoService(hikari()) }
}