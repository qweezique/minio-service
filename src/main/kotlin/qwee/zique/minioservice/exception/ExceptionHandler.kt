package qwee.zique.minioservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BucketNotFoundException::class, ObjectNotFoundException::class)
    fun handleNotFoundExceptions(ex: Exception): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(message = ex.message, code = HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(OriginalFilenameNotExistException::class)
    fun handleBadRequestExceptions(ex: Exception): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(message = ex.message, code = HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST
        )
}