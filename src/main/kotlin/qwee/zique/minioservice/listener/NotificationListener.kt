package qwee.zique.minioservice.listener

import mu.KLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import qwee.zique.minioservice.config.JavaMailSenderConfig
import qwee.zique.minioservice.event.FileUploadedEvent
import qwee.zique.minioservice.service.EmailNotificationService

@Component
@ConditionalOnBean(JavaMailSenderConfig::class)
class NotificationListener(
    private val mailService: EmailNotificationService
) {
    companion object : KLogging()

    @Async
    @EventListener(FileUploadedEvent::class)
    fun mailNotificationHandle(event: FileUploadedEvent) {
        event.email?.let { email ->
            logger.info { "Sending e-mail notification to: $email" }
            mailService.sendFileUploadedNotification(
                fileName = event.fileName,
                bucketName = event.bucketName, receiver = email
            )
        }
    }
}