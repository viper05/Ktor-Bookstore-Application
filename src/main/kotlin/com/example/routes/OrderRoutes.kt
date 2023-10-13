package com.example.routes

import com.example.validations.orderValidation
import db.repositories.OrderRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(val bookId: Int, val quantity:Int, val billingAddress: String)
fun Route.orderRoutes(db: OrderRepository){
    route("/v1/orders"){
        post{
            val parameters = call.receive<OrderRequest>()
            try {
                val validationResult = orderValidation.validate(parameters)
                if(validationResult.errors.isEmpty()) {
                    val res = db.createOrder(parameters.bookId, parameters.quantity, parameters.billingAddress)
                    if (res == 0) {
                        call.respondText(
                            "Requested quantity is more than the available",
                            status = HttpStatusCode.BadRequest
                        )
                    }
                    db.run { getOrderById((res!!).toInt()) }?.let { it1 -> call.respond(it1) }
                }
                else{
                    call.respond(ErrorResponse(validationResult.errors.map {it.dataPath +" " + it.message }))
                }
            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        get{
            try {
                call.respond(db.getAllOrder())
            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
        get("/{orderId}"){
            val orderId = call.parameters["orderId"]
            val res = db.getOrderById(orderId!!.toInt())
            try {
                if(res == null){
                    call.respondText("orderId $orderId not found")
                }
                else{
                    call.respond(res)
                }
            } catch (e: Throwable) {
                call.respond("${e.message}")
            }
        }
    }
}

