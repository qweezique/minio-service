package qwee.zique.minioservice.config

import io.minio.MinioClient
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig(
    @Value("\${minio.access.name}")
    private val accessKey: String,
    @Value("\${minio.access.secret}")
    private val accessSecret: String,
    @Value("\${minio.url:http://localhost:13666}")
    private val minioUrl: String
) {
    companion object : KLogging()

    @Bean
    fun minioClient(): MinioClient =
        MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, accessSecret)
            .build()
            .also { logger.info { "MinioClient configured: $it}" } }
}