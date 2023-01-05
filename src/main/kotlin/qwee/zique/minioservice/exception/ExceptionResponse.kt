package qwee.zique.minioservice.exception

import java.time.Instant

data class ExceptionResponse(
    val timestamp: Instant = Instant.now(),
    val message: String?,
    val code: Int,
)
