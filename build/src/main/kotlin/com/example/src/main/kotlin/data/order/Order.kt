package data.order

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val orderId: Int,
    val bookId:Int,
    val billingAddress: String,
    val totalPrice:Int,
    val quantity: Int
)