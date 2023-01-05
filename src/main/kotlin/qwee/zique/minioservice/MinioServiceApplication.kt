package qwee.zique.minioservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MinioServiceApplication

fun main(args: Array<String>) {
	runApplication<MinioServiceApplication>(*args)
}
