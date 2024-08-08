package org.example.endpointmonitoring.exceptions

import org.springframework.http.HttpStatus

class SpecialException(val statusCode: HttpStatus, message: String) : RuntimeException(){


}
