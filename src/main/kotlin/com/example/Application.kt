package com.example

import com.example.plugins.*
import db.repositories.BookRepository

import db.connection.DataSource
import io.ktor.server.application.*
import io.ktor.server.netty.Netty
import io.ktor.server.engine.*



fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DataSource.init()
    configureSerialization()
    configureStatusPages()
    configureRouting()

}


