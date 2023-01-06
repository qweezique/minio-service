package qwee.zique.minioservice.event

import org.springframework.context.ApplicationEvent

data class FileUploadedEvent(
    val fileName: String,
    val bucketName: String,
    val email: String?
) : ApplicationEvent(fileName)
