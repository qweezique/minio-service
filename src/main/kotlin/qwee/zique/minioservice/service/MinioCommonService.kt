package qwee.zique.minioservice.service

import io.minio.BucketExistsArgs
import io.minio.MinioClient
import mu.KLogging
import org.springframework.stereotype.Service
import qwee.zique.minioservice.Constants.MinioService.BUCKET_WITH_NAME

@Service
class MinioCommonService(
    private val minioClient: MinioClient
) {
    companion object : KLogging()

    fun isBucketExist(bucketName: String): Boolean =
        minioClient.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()
        ).apply {
            if (this) logger.info { "$BUCKET_WITH_NAME '$bucketName' exist" }
            else logger.info { "There is no $BUCKET_WITH_NAME '$bucketName'" }
        }
}