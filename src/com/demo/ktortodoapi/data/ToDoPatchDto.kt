package com.demo.ktortodoapi.data

data class ToDoPatchDto(var name: String?, var description: String?, var done: Boolean? = false)