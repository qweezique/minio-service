package qwee.zique.minioservice.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import qwee.zique.minioservice.exception.OriginalFilenameNotExistException
import qwee.zique.minioservice.service.MinioFileService

@RestController
@RequestMapping("file")
class FileController(
    private val fileService: MinioFileService
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

    @PostMapping(value = ["{bucketName}/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Upload file to bucket using 'content' key", description = "You can upload any types of files")
    fun uploadFile(
        @PathVariable bucketName: String, @RequestPart(value = "content", required = true) file: MultipartFile
    ): String {
        fileService.upload(
            bucketName = bucketName,
            fileName = file.originalFilename
                ?: throw OriginalFilenameNotExistException("$file must have original file name"),
            content = file.bytes,
        )
        return "${file.originalFilename} uploaded to $bucketName"
    }

    @DeleteMapping("{bucketName}/{objectName}/remove")
    @Operation(summary = "Delete file from bucket using original file name")
    fun removeFile(@PathVariable bucketName: String, @PathVariable objectName: String): ResponseEntity<String> {
        fileService.remove(bucketName, objectName)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}