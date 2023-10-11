package db.connection

import data.book.Book
import data.dao.OrderDao
import data.order.Order
import data.order.OrderTable
import db.repositories.BookRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement


fun rowToOrder(row: ResultRow): Order {
    return Order(
        orderId = row[OrderTable.orderId],
        bookId = row[OrderTable.bookId],
        billingAddress = row[OrderTable.billingAddress],
        quantity = row[OrderTable.quantity],
        totalPrice = row[OrderTable.totalPrice]

    )
}
class OrderRepository: OrderDao{
    override suspend fun createOrder(bookId: Int, quantity: Int, billingAddress: String): Order? {
        val book: Book? = BookRepository().getBookById(bookId)
        val newQuantity = (book!!.quantity)-quantity
        val res = BookRepository().updateById(bookId, book.title, book.author, book.price, newQuantity)

        val totalPrice = quantity*book.price
        return addOrder(bookId, billingAddress, quantity, totalPrice)

    }

    override suspend fun getAllOrder(): List<Order> =
        DataSource.dbQuery {
            OrderTable.selectAll().mapNotNull {
                rowToOrder(it)
            }
        }

    override suspend fun addOrder(
        bookId: Int,
        billingAddress: String,
        quantity: Int,
        totalPrice: Int
    ): Order? {
        var statement: InsertStatement<Number>?=null
        DataSource.dbQuery {
            statement = OrderTable.insert { order ->
                order[OrderTable.bookId] = bookId
                order[OrderTable.billingAddress] = billingAddress
                order[OrderTable.quantity]=quantity
                order[OrderTable.totalPrice] = totalPrice
            }

        }
        return statement?.resultedValues?.let { rowToOrder(it.get(0)) }
    }

}