package qwee.zique.minioservice.service

import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.RemoveBucketArgs
import io.minio.messages.Bucket
import org.springframework.stereotype.Service
import qwee.zique.minioservice.Constants.MinioService.BUCKET_WITH_NAME

@Service
class MinioBucketService(
    private val minioClient: MinioClient
) : MinioCommonService(minioClient) {

    fun getAllBuckets(): List<Bucket> = minioClient.listBuckets()

    fun getBucketsNames(): List<String> =
        getAllBuckets().map { it.name() }.toList()

    fun create(bucketName: String) {
        if (!isBucketExist(bucketName)) makeBucket(bucketName)
    }

    fun remove(bucketName: String) {
        if (isBucketExist(bucketName)) removeBucket(bucketName)
    }

    private fun removeBucket(bucketName: String) =
        minioClient.removeBucket(
            RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build()
                .also { logger.info { "$BUCKET_WITH_NAME '$bucketName' removed" } }
        )

    private fun makeBucket(bucketName: String) =
        minioClient.makeBucket(
            MakeBucketArgs.builder()
                .bucket(bucketName).build()
                .also { logger.info { "$BUCKET_WITH_NAME '$bucketName' created" } }
        )
}