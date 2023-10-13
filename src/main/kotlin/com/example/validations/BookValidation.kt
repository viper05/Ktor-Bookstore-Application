package com.example.validations


import com.example.routes.PostBookRequest
import com.example.routes.UpdateBookRequest
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum
import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*

//fun RequestValidationConfig.bookValidation() {
//    validate<Parameters> { book ->
//        if (book["title"] == null) {
//            ValidationResult.Invalid("title must not be empty")
//        } else if (book["author"]==null) {
//            ValidationResult.Invalid("Author must not be empty")
//        } else if (book["price"]==null ) {
//            ValidationResult.Invalid("Price must be positive value or zero")
//        } else if (book["quantity"]==null) {
//            ValidationResult.Invalid("Quantity must be positive number or zero")
//        }else {
//            ValidationResult.Valid
//        }
//    }
//}

val postBookValidation = Validation<PostBookRequest> {

    PostBookRequest::title required {
        minLength(3)
        maxLength(20)
    }
    PostBookRequest::author required {
        minLength(3)
        maxLength(200)
    }
    PostBookRequest::price required {
        addConstraint("price must be greater than 0") { it > 0 }
        minimum(100)

    }
    PostBookRequest::quantity {
        addConstraint("price must be greater than 0") { it > 0 }
        minimum(100)
    }
}

val updateBookValidation = Validation<UpdateBookRequest> {
    UpdateBookRequest::bookId required {

    }
    UpdateBookRequest::title required {
        minLength(3)
        maxLength(200)
    }
    UpdateBookRequest::author required {
        minLength(3)
        maxLength(200)
    }
    UpdateBookRequest::price required {
        addConstraint("price must be greater than 0") { it > 0 }
        minimum(100)

    }
    UpdateBookRequest::quantity {
        addConstraint("price must be greater than 0") { it > 0 }
        minimum(0)
    }
}