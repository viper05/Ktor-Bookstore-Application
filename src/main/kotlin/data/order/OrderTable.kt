package data.order


import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object OrderTable: Table(){
    val orderId: Column<Int> = OrderTable.integer("orderId").autoIncrement()
    val bookId: Column<Int> = OrderTable.integer("bookId")
    val bookTitle: Column<String> = OrderTable.varchar("bookTitle", 500)
    val billingAddress: Column<String> = OrderTable.varchar("billingAddress", 500)
    val quantity: Column<Int> = OrderTable.integer("quantity")
    val totalPrice: Column<Int> = OrderTable.integer("totalPrice")


    override val primaryKey: Table.PrimaryKey = PrimaryKey(orderId)
}