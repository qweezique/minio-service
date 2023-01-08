package qwee.zique.minioservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableRetry
class MinioServiceApplication

fun main(args: Array<String>) {
	runApplication<MinioServiceApplication>(*args)
}
