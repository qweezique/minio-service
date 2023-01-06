package qwee.zique.minioservice.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.context.ApplicationEventPublisher
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import qwee.zique.minioservice.event.FileUploadedEvent
import qwee.zique.minioservice.exception.OriginalFilenameNotExistException
import qwee.zique.minioservice.service.MinioFileService

@RestController
@RequestMapping("file")
class FileController(
    private val fileService: MinioFileService,
    private val publisher: ApplicationEventPublisher,
) {
    @GetMapping("{bucketName}")
    @Operation(summary = "Getting list of files in bucket")
    fun getFilesInBucket(@PathVariable bucketName: String): List<String> =
        fileService.getListOfFilesInBucket(bucketName)

    @GetMapping("{bucketName}/{objectName}/download")
    @Operation(summary = "Download file from bucket by original file name")
    fun downloadFile(
        @PathVariable bucketName: String, @PathVariable objectName: String
    ): ResponseEntity<ByteArrayResource> {
        val resource = ByteArrayResource(fileService.download(bucketName, objectName))
        return ResponseEntity.ok().contentLength(resource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=${resource.filename}").body(resource)
    }

    //TODO: толстый контроллер -> понять и простить
    @PostMapping(value = ["{bucketName}/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "Upload file to bucket using 'content' key and send e-mail notification about this when email field not empty",
    )
    fun uploadFile(
        @PathVariable bucketName: String,
        @RequestPart(value = "content", required = true) file: MultipartFile,
        @RequestPart(value = "email", required = false) email: String?
    ): String {
        val fileName = file.originalFilename ?: throw OriginalFilenameNotExistException("$file missing original name")
        fileService.upload(
            bucketName = bucketName,
            fileName = fileName,
            content = file.bytes,
        )
        return StringBuilder()
            .apply {
                this.append("$fileName uploaded to $bucketName")
                email?.let { this.append(" and sent notification to $email") } //TODO: При spring.mail.notification.enabled: false все равно придет ответ об отправке на почту
            }
            .toString()
            .also { publisher.publishEvent(FileUploadedEvent(fileName, bucketName, email)) }
    }

    @DeleteMapping("{bucketName}/{objectName}/remove")
    @Operation(summary = "Delete file from bucket using original file name")
    fun removeFile(@PathVariable bucketName: String, @PathVariable objectName: String): ResponseEntity<String> {
        fileService.remove(bucketName, objectName)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}