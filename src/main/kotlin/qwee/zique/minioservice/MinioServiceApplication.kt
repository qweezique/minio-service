package qwee.zique.minioservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class MinioServiceApplication

fun main(args: Array<String>) {
	runApplication<MinioServiceApplication>(*args)
}
