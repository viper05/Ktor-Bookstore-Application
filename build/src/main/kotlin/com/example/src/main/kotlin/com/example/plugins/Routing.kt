package com.example.plugins

import com.example.routes.bookRoutes
import com.example.routes.orderRoutes
import db.connection.OrderRepository
import db.repositories.BookRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

// Job: Routes different endpoints

fun Application.configureRouting() {
    routing {
        bookRoutes(BookRepository())
        orderRoutes(OrderRepository())

    }
}


