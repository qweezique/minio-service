package qwee.zique.minioservice.service

import mu.KLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import qwee.zique.minioservice.Constants.Mail.FROM
import qwee.zique.minioservice.Constants.Mail.SUBJECT
import qwee.zique.minioservice.config.JavaMailSenderConfig
import java.net.InetAddress
import javax.mail.internet.InternetAddress

@Service
@ConditionalOnBean(value = [JavaMailSenderConfig::class])
class EmailNotificationService(
    private val javaMailSender: JavaMailSender,
) {
    companion object : KLogging()

    @Async
    @Retryable(maxAttempts = 3, value = [Exception::class], backoff = Backoff(delay = 300))
    fun sendFileUploadedNotification(fileName: String, bucketName: String, receiver: String) {
        logger.info { "Try to sent e-mail to: $receiver" }
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        with(helper) {
            setFrom(InternetAddress(FROM, FROM))
            setTo(receiver)
            setSubject(SUBJECT)
            setText(formText(fileName, bucketName), true)
        }
        javaMailSender.send(message).also { logger.info { "e-mail notification sent to: '$receiver'" } }
    }

    private fun formText(fileName: String, bucketName: String) = """
                |<p>You successfully upload <b>$fileName</b> to <b>$bucketName</b>
                |<br>Congrats &#128579;</p>
                |<p><a href="${InetAddress.getLocalHost().hostAddress}/file/$bucketName/$fileName/download">&#128048; Download this file</a></p>
                """.trimMargin()

    @Recover
    fun sendFileUploadedNotificationRecover(
        ex: Exception,
        fileName: String,
        bucketName: String,
        receiver: String
    ) {
        logger.info { "E-mail notification about '$fileName' not sent to: $receiver, because: ${ex.message}" }
    }
}