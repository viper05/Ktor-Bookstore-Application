package com.example.routes

import db.connection.OrderRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*


fun Route.orderRoutes(db:OrderRepository){
    route("/v1/order"){
        post{
            val parameter = call.receive<Parameters>()
            val bookId = parameter["bookId"] ?: return@post respondWithUnauthorizedRequest()
            val quantity = parameter["quantity"] ?: return@post respondWithUnauthorizedRequest()
            val billingAddress = parameter["billingAddress"] ?: return@post respondWithUnauthorizedRequest()
            try {
                val res = db.createOrder(bookId.toInt(), quantity.toInt(), billingAddress)
                call.respondText(
                    ""
                )
                call.respond(db.getAllOrder())

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