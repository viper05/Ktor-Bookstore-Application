package com.example.routes

import com.example.validations.postBookValidation
import com.example.validations.updateBookValidation
import db.repositories.BookRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable

@Serializable
data class PostBookRequest(val title: String, val author: String, val price: Int, val quantity: Int)

@Serializable
data class UpdateBookRequest(val bookId:Int,val title: String, val author: String, val price: Int, val quantity: Int)
@Serializable
data class ErrorResponse(val error: List<String>)


fun Route.bookRoutes(db: BookRepository){
    route("/v1/books"){
        post{
            val parameters = call.receive<PostBookRequest>()
            try {
                val validationResult = postBookValidation.validate(parameters)
                if(validationResult.errors.isEmpty()){
                    val res = db.insertAndGetId(parameters.title,parameters.author,parameters.price,parameters.quantity)
                    db.run { getBookById((res!!).toInt()) }?.let { it1 -> call.respond(it1) }
                }
                else{
                    call.respond(ErrorResponse(validationResult.errors.map {it.dataPath +" " + it.message }))
                }
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
            val parameters = call.receive<UpdateBookRequest>()
            try {
                val validationResult = updateBookValidation.validate(parameters)
                if(validationResult.errors.isEmpty()) {
                    val res = db.updateById(
                        parameters.bookId,
                        parameters.title,
                        parameters.author,
                        parameters.price,
                        parameters.quantity
                    )
                    if (res == 1) {
                        db.run { getBookById((parameters.bookId)) }?.let { it1 -> call.respond(it1) }
                    } else {
                        call.respondText("bookId not found")
                    }
                }
                else{
                    call.respond(ErrorResponse(validationResult.errors.map {it.dataPath +" " + it.message }))
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


