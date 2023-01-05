package qwee.zique.minioservice.service

import io.minio.*
import io.minio.messages.Item
import org.apache.commons.compress.utils.IOUtils
import org.springframework.stereotype.Service
import qwee.zique.minioservice.Constants.BUCKET_WITH_NAME
import qwee.zique.minioservice.exception.BucketNotFoundException
import qwee.zique.minioservice.exception.ObjectNotFoundException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Service
class MinioFileService(
    private val minioClient: MinioClient
) : MinioCommonService(minioClient) {

    private fun getFiles(bucketName: String): MutableIterable<Result<Item>> =
        if (isBucketExist(bucketName)) getFilesInBucket(bucketName)
        else throw BucketNotFoundException("$BUCKET_WITH_NAME '$bucketName' not present")

    private fun getFilesInBucket(bucketName: String) =
        minioClient.listObjects(
            ListObjectsArgs
                .builder()
                .bucket(bucketName)
                .build()
        )

    fun getListOfFilesInBucket(bucketName: String): List<String> =
        getFiles(bucketName).map { it.get().objectName() }.toList()

    fun download(bucketName: String, fileName: String): ByteArray {
        val stream = InputStream.nullInputStream().use { getObjectResponse(bucketName, fileName) }
        return IOUtils.toByteArray(stream)
    }

    private fun getObjectResponse(bucketName: String, objectName: String) =
        minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .build()
        ) ?: throw ObjectNotFoundException("Object with path: '$bucketName/$objectName' not found")

    fun upload(bucketName: String, fileName: String, content: ByteArray) {
        if (isBucketExist(bucketName)) uploadToBucket(bucketName, fileName, content)
        else throw BucketNotFoundException("Can't upload file, because $BUCKET_WITH_NAME '$bucketName' not exist")
    }

    private fun uploadToBucket(bucketName: String, fileName: String, content: ByteArray) {
        val file = File(fileName).apply { canRead(); canWrite() }
        val fileOutputStream = FileOutputStream(file).also { it.write(content) }
        minioClient.uploadObject(
            UploadObjectArgs
                .builder()
                .bucket(bucketName)
                .`object`(file.name)
                .filename(fileName)
                .build()
        ).also {
            fileOutputStream.close()
            logger.info { "File with name: '$fileName' uploaded to bucket: '$bucketName'" }
        }
    }

    fun remove(bucketName: String, fileName: String) {
        minioClient.removeObject(
            RemoveObjectArgs
                .builder()
                .bucket(bucketName)
                .`object`(fileName)
                .build()
        ).also { logger.info { "File with name: '$fileName' removed from bucket: '$bucketName'" } }
    }
}