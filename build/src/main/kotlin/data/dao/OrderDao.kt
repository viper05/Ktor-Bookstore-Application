package data.dao

import data.order.Order

interface OrderDao {
    suspend fun createOrder(bookId: Int, quantity: Int, billingAddress: String): Order?

    suspend fun getAllOrder(): List<Order> ?

    suspend fun addOrder(
        bookId: Int,
        billingAddress: String,
        quantity:Int,
        totalPrice: Int
    ): Order?
}