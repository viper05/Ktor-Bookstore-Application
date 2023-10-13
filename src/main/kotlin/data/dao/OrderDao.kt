package data.dao

import data.order.Order

interface OrderDao {
    suspend fun createOrder(bookId: Int, quantity: Int, billingAddress: String): Int?

    suspend fun getOrderById(orderId:Int): Order?

    suspend fun getAllOrder(): List<Order> ?

    suspend fun addOrder(
        bookId: Int,
        bookTitle: String,
        billingAddress: String,
        quantity:Int,
        totalPrice: Int
    ): Int?
}