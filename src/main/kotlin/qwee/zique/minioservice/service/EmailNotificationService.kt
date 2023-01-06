package qwee.zique.minioservice.service

import mu.KLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import qwee.zique.minioservice.Constants.Mail.FROM
import qwee.zique.minioservice.Constants.Mail.SUBJECT
import qwee.zique.minioservice.config.JavaMailSenderConfig
import javax.mail.internet.InternetAddress

@Service
@ConditionalOnBean(value = [JavaMailSenderConfig::class])
class EmailNotificationService(
    private val javaMailSender: JavaMailSender,
) {
    companion object : KLogging()

    @Async
    fun sendFileUploadedNotification(fileName: String, bucketName: String, receiver: String) {
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        with(helper) {
            setFrom(InternetAddress(FROM, FROM))
            setTo(receiver)
            setSubject(SUBJECT)
            setText("<p>You successfully upload <b>$fileName</b> to <b>$bucketName</b><br>Congrats &#128579;</p>", true)
        }
        javaMailSender.send(message).also { logger.info { "e-mail notification sent to: $receiver" } }
    }
}