package qwee.zique.minioservice.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import qwee.zique.minioservice.service.MinioBucketService

@RestController
@RequestMapping("bucket")
class BucketController(
    private val bucketService: MinioBucketService
) {
    @GetMapping
    @Operation(summary = "Getting list of buckets")
    fun getBucketsNames(): List<String> =
        bucketService.getBucketsNames()

    @GetMapping("{bucketName}/create")
    @Operation(summary = "Create new bucket")
    fun createBucket(@PathVariable bucketName: String): List<String> {
        bucketService.create(bucketName)
        return getBucketsNames()
    }

    @GetMapping("{bucketName}/remove")
    @Operation(summary = "Remove bucket")
    fun removeBucket(@PathVariable bucketName: String): List<String> {
        bucketService.remove(bucketName)
        return getBucketsNames()
    }
}