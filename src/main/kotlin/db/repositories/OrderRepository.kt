package db.repositories

import data.book.Book
import data.dao.OrderDao
import data.order.Order
import data.order.OrderTable
import db.connection.DataSource
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement


fun rowToOrder(row: ResultRow): Order {
    return Order(
        orderId = row[OrderTable.orderId],
        bookId = row[OrderTable.bookId],
        bookTitle = row[OrderTable.bookTitle],
        billingAddress = row[OrderTable.billingAddress],
        quantity = row[OrderTable.quantity],
        totalPrice = row[OrderTable.totalPrice]

    )
}
class OrderRepository: OrderDao{
    override suspend fun createOrder(bookId: Int, quantity: Int, billingAddress: String): Int? {
        val book: Book? = BookRepository().getBookById(bookId)
        if(book!!.quantity < quantity){
            return 0
        }
        val newQuantity = (book.quantity)-quantity
        val res = BookRepository().updateById(bookId, book.title, book.author, book.price, newQuantity)

        val totalPrice = quantity*book.price
        return addOrder(bookId, book.title, billingAddress, quantity, totalPrice)

    }

    override suspend fun getOrderById(orderId: Int): Order?  =
        DataSource.dbQuery {
            OrderTable.select { OrderTable.orderId.eq(orderId) }.map {
                rowToOrder(it)

            }.singleOrNull()
        }

    override suspend fun getAllOrder(): List<Order> =
        DataSource.dbQuery {
            OrderTable.selectAll().mapNotNull {
                rowToOrder(it)
            }
        }


    override suspend fun addOrder(
        bookId: Int,
        bookTitle: String,
        billingAddress: String,
        quantity: Int,
        totalPrice: Int
    ): Int? {
        var statement: InsertStatement<Number>?=null
        DataSource.dbQuery {
            statement = OrderTable.insert { order ->
                order[OrderTable.bookId] = bookId
                order[OrderTable.bookTitle] = bookTitle
                order[OrderTable.billingAddress] = billingAddress
                order[OrderTable.quantity] = quantity
                order[OrderTable.totalPrice] = totalPrice
            }

        }
        return statement?.get(OrderTable.orderId)
    }

}