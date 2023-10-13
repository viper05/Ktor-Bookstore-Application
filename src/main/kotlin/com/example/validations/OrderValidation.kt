package com.example.validations

import com.example.routes.OrderRequest
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum

val orderValidation = Validation<OrderRequest> {
    OrderRequest::bookId required {

    }
    OrderRequest::quantity {
        addConstraint("price must be greater than 0") { it > 0 }
        minimum(0)
    }
    OrderRequest::billingAddress required {
        minLength(3)
        maxLength(20)
    }
}