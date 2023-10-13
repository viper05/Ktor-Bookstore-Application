package data.dao

import data.book.Book

interface BookDao {
    suspend fun  insertAndGetId(
        title: String,
        author: String,
        price: Int,
        quantity:Int
    ): Int?

    suspend fun getAllBooks(): List<Book>?

    suspend fun getBookById(bookId: Int): Book?

    suspend fun  deleteById(bookId: Int): Int

    suspend fun updateById(bookId: Int,title: String, author: String, price: Int, quantity: Int):Int
}
