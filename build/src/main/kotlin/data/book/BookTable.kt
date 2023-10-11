package data.book

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object BookTable : Table(){
    val bookId:Column<Int> = integer("bookId").autoIncrement()
    val title:Column<String> = varchar("title",500)
    val author:Column<String> = varchar("author",500)
    val price:Column<Int> = integer("price")
    val quantity:Column<Int> = integer("quantity")


    override val primaryKey: PrimaryKey= PrimaryKey(bookId)


}