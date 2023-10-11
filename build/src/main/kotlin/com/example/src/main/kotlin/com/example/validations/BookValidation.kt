package com.example.validations


import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.bookValidation() {
    validate<Parameters> { book ->
        if (book["title"] == null) {
            ValidationResult.Invalid("title must not be empty")
        } else if (book["author"]==null) {
            ValidationResult.Invalid("Author must not be empty")
        } else if (book["price"]==null ) {
            ValidationResult.Invalid("Price must be positive value or zero")
        } else if (book["quantity"]==null) {
            ValidationResult.Invalid("Quantity must be positive number or zero")
        }else {
            ValidationResult.Valid
        }
    }
}

//fun