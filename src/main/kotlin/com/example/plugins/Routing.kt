package com.example.plugins

import db.connection.BookRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Application.configureRouting(
    db:BookRepository
) {
    routing {
        get("/") {
            call.respondText("Hello world")
        }
        post("/book"){
            val parameter = call.receive<Parameters>()
            val title = parameter["title"] ?: return@post call.respondText(
                text = "MISSING FIELD",
                status = HttpStatusCode.Unauthorized
            )
            val author = parameter["author"] ?: return@post call.respondText(
                text = "MISSING FIELD",
                status = HttpStatusCode.Unauthorized
            )
            val price = parameter["price"] ?: return@post call.respondText(
                text = "MISSING FIELD",
                status = HttpStatusCode.Unauthorized
            )
            try {
                val book = db.insert(title, author, price.toInt())
                call.respondText("Success")

            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        get("/book") {
            try {
                call.respond(db.getAllBooks())

            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        delete("/book/{bookId}") {
            val bookId = call.parameters["bookId"] ?: return@delete call.respondText(
                "Missing bookId",
                status = HttpStatusCode.Unauthorized
            )
            val res = db.deleteById(bookId.toInt())

            try {
                if(res==1){
                    call.respondText("Deleted Successfully")
                }
                else{
                    call.respondText("bookId $bookId not found")
                }

            }catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        get("/book/{bookId}"){
            val bookId = call.parameters["bookId"] ?: return@get call.respondText(
                "Missing bookId",
                status = HttpStatusCode.Unauthorized
            )
            val res = db.getBookById(bookId.toInt())
            try {
                if(res == null){
                    call.respondText("bookId $bookId not found")

                }
                else{
                    call.respond(db.getBookById(bookId.toInt())!!)
                }
            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        put("/book"){
            val parameter = call.receive<Parameters>()
            val bookId = parameter["bookId"] ?: return@put call.respondText(
                text = "MISSING FIELD",
                status = HttpStatusCode.Unauthorized
            )
            val title = parameter["title"] ?: return@put call.respondText(
                text = "MISSING FIELD",
                status = HttpStatusCode.Unauthorized
            )
            val author = parameter["author"] ?: return@put call.respondText(
                text = "MISSING FIELD",
                status = HttpStatusCode.Unauthorized
            )
            val price = parameter["price"] ?: return@put call.respondText(
                text = "MISSING FIELD",
                status = HttpStatusCode.Unauthorized
            )
            try {
                val res = db.update(bookId.toInt(), title, author, price.toInt())
                if(res==1){
                    call.respond(db.getAllBooks())
                }else{
                    call.respondText("bookId not found")

                }
            } catch (e: Throwable) {
                call.respond("${e.message}")
            }

        }

    }
}
