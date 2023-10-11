package com.example.routes

import db.repositories.BookRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable

@Serializable
data class BookRequest(val title: String,val author: String,val price: Int, val quantity: Int)

fun Route.bookRoutes(db: BookRepository){
    route("/v1/books"){
        post{
            val parameters = call.receive<Parameters>()
//            val title = parameters.title ?: return@post respondWithUnauthorizedRequest()
//            val author =parameters.author ?: return@post respondWithUnauthorizedRequest()
//            val price = parameters.price ?: return@post respondWithUnauthorizedRequest()
//            val quantity = parameters.quantity ?: return@post respondWithUnauthorizedRequest()
            try {
                val res = db.insert(
                    parameters["title"].toString(),
                    parameters["author"].toString(),
                    parameters["price"]!!.toInt(),
                    parameters["quantity"]!!.toInt())
                call.respond(db.getAllBooks())

            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        get {
            try {
                call.respond(db.getAllBooks())

            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        delete("/{bookId}") {
            val bookId = call.parameters["bookId"] ?: return@delete respondWithUnauthorizedRequest()
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
        get("/{bookId}"){
            val bookId = call.parameters["bookId"] ?: return@get respondWithUnauthorizedRequest()
            val res = db.getBookById(bookId.toInt())
            try {
                if(res == null){
                    call.respondText("bookId $bookId not found")

                }
                else{
                    call.respond(res)
                }
            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        put{
            val parameter = call.receive<Parameters>()
            val bookId = parameter["bookId"] ?: return@put respondWithUnauthorizedRequest()
            val title = parameter["title"] ?: return@put respondWithUnauthorizedRequest()
            val author = parameter["author"] ?: return@put respondWithUnauthorizedRequest()
            val price = parameter["price"] ?: return@put respondWithUnauthorizedRequest()
            val quantity = parameter["quantity"] ?: return@put respondWithUnauthorizedRequest()
            try {
                val res = db.updateById(bookId.toInt(), title, author, price.toInt(), quantity.toInt())
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

private suspend fun PipelineContext<Unit, ApplicationCall>.respondWithUnauthorizedRequest() {
    call.respondText(
        text = "MISSING FIELD",
        status = HttpStatusCode.Unauthorized
    )
}


