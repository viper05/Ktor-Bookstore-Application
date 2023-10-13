package db.repositories

import data.book.Book
import data.book.BookTable
import data.dao.BookDao
import db.connection.DataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

fun rowToBook(row: ResultRow): Book {
    return Book(
        bookId = row[BookTable.bookId],
        title = row[BookTable.title],
        author = row[BookTable.author],
        price = row[BookTable.price],
        quantity = row[BookTable.quantity]
    )
}

open class BookRepository: BookDao {
    override suspend fun insertAndGetId(title: String, author: String, price: Int, quantity: Int): Int? {
        var statement: InsertStatement<Number>?=null
        DataSource.dbQuery {
            statement = BookTable.insert { book ->
                book[BookTable.title] = title
                book[BookTable.author] = author
                book[BookTable.price] = price
                book[BookTable.quantity] = quantity
            }
        }
        return statement?.get(BookTable.bookId)

    }

    override suspend fun getAllBooks(): List<Book> =
        DataSource.dbQuery {
            BookTable.selectAll().mapNotNull {
                rowToBook(it)
            }
        }

    override suspend fun getBookById(bookId: Int): Book? =
        DataSource.dbQuery {
            BookTable.select { BookTable.bookId.eq(bookId) }.map {
                rowToBook(it)

            }.singleOrNull()
        }

    override suspend fun deleteById(bookId: Int): Int =
        DataSource.dbQuery {
            BookTable.deleteWhere { BookTable.bookId.eq(bookId) }
        }


    override suspend fun updateById(bookId: Int, title: String, author: String, price: Int, quantity: Int): Int =
        DataSource.dbQuery {
            BookTable.update({ BookTable.bookId.eq(bookId) }) { book ->
                book[BookTable.title] = title
                book[BookTable.author] = author
                book[BookTable.price] = price
                book[BookTable.quantity] = quantity
            }

        }
}